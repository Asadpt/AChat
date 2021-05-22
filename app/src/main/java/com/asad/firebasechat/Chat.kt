package com.asad.firebasechat

data class Chat (
        var senderId:String? = null,
        var receiverId:String? = null,
        var id:String? = null,
        var text:String? = null,
        var time:Long = 0
)