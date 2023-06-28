package com.noritz.whatsup

//data class User (val id : String?, val username : String?, val phone : String?, val image : String?)
class  User {

    var id : String ? = null
    var username : String ? = null
    var phone : String ? = null
    var image : String ? = null

    constructor(){}
    constructor( id : String?,  username : String?,  phone : String?,  image : String?) {
        this.id = id
        this.username = username
        this.phone = phone
        this.image = image
    }

}