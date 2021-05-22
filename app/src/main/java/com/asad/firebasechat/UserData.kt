package com.asad.firebasechat

import java.io.Serializable

data class UserData (
    var userId:String? = null,
    var name:String? = null,
    var emailId:String? = null,
    var password:String? = null,
    var profileImageUrl:String? = null,
    var token:String? = null,
    var online:Boolean = false
):Serializable