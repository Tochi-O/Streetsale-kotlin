package com.example.streetsale

class Saleitem {
     var name: String=""
     var description: String=""
     var price: String=""
     var img: String=""
     var priceBids: ArrayList<aBid> =  ArrayList<aBid>()
    var sold: Boolean=false
     var boughtById: String=""
     var boughtByName: String=""
     var itemId: String=""
     var saleId: String=""
     var adminId: String=""
     var addr: String=""

}

class aBid{
     var price: Int=0
     var userId: String=""
     var userName: String=""
    var soldTo: Boolean=false
     var bidAdmin:String=""
     var idItem: String=""
    var boughtId: String=""
    var saleId: String=""


    // var itemObj: Saleitem = Saleitem()

}

//class soldItem{
//    var
//}