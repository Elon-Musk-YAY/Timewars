package com.akshardesai.timewars.map

import java.io.*

object FileUtil {
    @Throws(IOException::class)
    fun copy(source: File, destination: File) {
        if (source.isDirectory) {
            if (!destination.exists()) {
                val made = destination.mkdir()
                if (!made) throw IOException()
            }
            val files = source.list() ?: return
            for (file in files) {
                val newSource = File(source, file)
                val newDestination = File(destination, file)
                copy(newSource, newDestination)
            }
        } else {
            val `in`: InputStream = FileInputStream(source)
            val out: OutputStream = FileOutputStream(destination)
            val buffer = ByteArray(1024)
            var length: Int
            while (`in`.read(buffer).also { length = it } > 0) {
                out.write(buffer, 0, length)
            }
            `in`.close()
            out.close()
        }
    }
}