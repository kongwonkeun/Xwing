package com.rdt.xwing

interface BTCallback {

    fun on_connect()
    fun on_connect_err(e: Exception)
    fun on_io_err(e: Exception)
    fun on_recv(data: ByteArray)

}

/* EOF */