package me.eagzzycsl.wecheat.task

import android.content.Context
import android.support.v4.content.FileProvider
import com.google.gson.Gson
import me.eagzzycsl.wecheat.model.ShuttleModel
import me.eagzzycsl.wecheat.utils.MyLogger
import me.eagzzycsl.wecheat.utils.RunConfig
import me.eagzzycsl.wecheat.utils.Security
import okhttp3.*
import android.R.attr.name
import me.eagzzycsl.wecheat.utils.ConstantString
import java.io.IOException
import okio.Okio
import okio.BufferedSink
import java.io.File


object ShuttleService {
    private val GJsonInstance = Gson();
    private val ignoreRequest = Request.Builder().url("http://localhost").get().build();
    private var shuttleRequest = ignoreRequest;
    private val client = OkHttpClient();
    private val mediaTypeJSON = MediaType.parse("application/json; charset=utf-8")
    fun startTask(context: Context) {
        Thread({
            while (RunConfig.netThreadRunning) {
                try {
                    if (shuttleRequest == ignoreRequest) {
                        shuttleRequest = Request.Builder().url(
                                RunConfig.mainInterface
                        ).post(generatePostBody()).build();
                        MyLogger.Network.i("构造完成");
                    } else {
                        val response = client.newCall(shuttleRequest).execute();
                        MyLogger.Network.i("请求发出");
                        if (response.isSuccessful) {
                            val shuttleModel = dealResponse(response)
                            MyLogger.Network.i("请求到的model", shuttleModel.toString())
                            appendToActionQueue(shuttleModel, context);
                            shuttleRequest = ignoreRequest;
                        } else {
                            MyLogger.Exception.e(response.message());
                            MyLogger.Exception.e("" + response.code())
                            MyLogger.Exception.e("" + response.body()?.string())
                            MyLogger.Exception.e("请求失败");
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                Thread.sleep(RunConfig.shuttleInterval.toLong())
            }
        }).start();
    }

    private fun appendToActionQueue(shuttleModel: ShuttleModel, context: Context) {
        val messages = shuttleModel.getMsgList();
        val textActions = messages.filter { it.type == "text" }.map { it.generateAction() }.toTypedArray();
        ActionQueue.appendAll(textActions);
        return;
        val imgMessages = messages.filter { it.type == "img" }
        imgMessages.forEach({
            val uri = FileProvider.getUriForFile(
                    context, context.applicationContext.packageName,
                    context.externalCacheDir.resolve("avatar.jpg")
            )
            it.setImg(uri)
            ActionQueue.append(it.generateAction());
        })
        imgMessages.forEach({
            val request = okhttp3.Request.Builder().url(it.msg).build()
            client.newCall(request).enqueue(object : okhttp3.Callback { // 2
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    e.printStackTrace()
                }

                @Throws(IOException::class)
                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    val fileName = System.currentTimeMillis().toString();
                    val downloadedFile = File(context.externalCacheDir, fileName);
                    if (response.body() != null) {
                        val sink = Okio.buffer(Okio.sink(downloadedFile))
                        sink.writeAll(response.body()!!.source())
                        sink.close()
                    }
                    val uri = FileProvider.getUriForFile(
                            context, context.applicationContext.packageName,
                            context.externalCacheDir.resolve("avatar.jpg")
                    )
                    it.setImg(uri)
                    ActionQueue.append(it.generateAction());
                }
            })
        })

    }

    private fun dealResponse(response: Response): ShuttleModel {
        val body = response.body();
        val bodyText = body?.string();
        return if (body != null && bodyText != null) {
            MyLogger.Network.i("请求结果", bodyText);
            GJsonInstance.fromJson<ShuttleModel>(bodyText, ShuttleModel::class.java)
        } else {
            MyLogger.Network.i("请求结果", "为空");
            ShuttleModel("", "");
        }
    }

    private fun generatePostBody(): RequestBody {
        val msgList = MsgQueue.pollAll();
        val msgListStr = Security.encrypt(GJsonInstance.toJson(msgList), RunConfig.securityKey);
        val signature = calculateSignature(msgListStr, RunConfig.signatureKey);
        val bodyJSON = GJsonInstance.toJson(ShuttleModel(msgListStr, signature));
        return RequestBody.create(mediaTypeJSON, bodyJSON);
    }

    private fun calculateSignature(source: String, key: String): String {
        return Security.md5(source + key);
    }


}