package me.eagzzycsl.wecheat.utils

import java.nio.charset.Charset
import java.security.NoSuchAlgorithmException
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec
import javax.crypto.spec.IvParameterSpec

object Security {
    fun test() {
        val content = "谷歌：http://www.google.com"
        val key = "zhlxkaka"
        println("加密前：" + content)
        val encrypted = encrypt(content, key)
        println("加密后：" + encrypted)
        val decrypted = decrypt(encrypted ?: "", key)
        println("解密后：" + decrypted)
    }

    fun encrypt(content: String, keyStr: String): String {
        try {
            val keySpec = DESKeySpec(keyStr.toByteArray(Charset.forName("utf-8")))
            val keyFactory = SecretKeyFactory.getInstance("DES")
            val key = keyFactory.generateSecret(keySpec)
            val cipher = Cipher.getInstance("DES/CBC/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, key, IvParameterSpec(keySpec.key))
            val result = cipher.doFinal(content.toByteArray())
            return bytesToHex(result)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }

    fun decrypt(content: String, keyStr: String): String {
        try {
            val keySpec = DESKeySpec(keyStr.toByteArray())
            val keyFactory = SecretKeyFactory.getInstance("DES")
            val key = keyFactory.generateSecret(keySpec)
            val cipher = Cipher.getInstance("DES/CBC/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(keyStr.toByteArray()))
            val result = cipher.doFinal(hexToBytes(content))
            return String(result)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return "";
    }

    private fun bytesToHex(bytes: ByteArray): String {
        return bytes.joinToString("") { String.format("%02x", it) }
    }

    private fun hexToBytes(s: String): ByteArray {
        val data = ByteArray(s.length / 2)
        data.forEachIndexed { index, _ ->
            data[index] = ((Character.digit(s[index * 2], 16) shl 4) + Character.digit(s[index * 2 + 1], 16)).toByte()
        }
        return data;
    }

    fun md5(s: String): String {
        try {
            // Create MD5 Hash
            val digest = java.security.MessageDigest
                    .getInstance("MD5")
            digest.update(s.toByteArray())
            val messageDigest = digest.digest()

            // Create Hex String
            val hexString = StringBuilder()
            for (aMessageDigest in messageDigest) {
                var h = Integer.toHexString(0xFF and aMessageDigest.toInt())
                while (h.length < 2)
                    h = "0" + h
                hexString.append(h)
            }
            return hexString.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return ""
    }
}