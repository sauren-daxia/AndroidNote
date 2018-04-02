package com.test.jasonchen.mysdk.net

/**
 * Created by marti on 2018/3/3.
 */
fun main(args: Array<String>) {

        var a = object:A{
            override var b: String
                get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
                set(value) {}
            override val a: Int
                get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

        }

}

interface A{


    val a :Int



    var b : String
    get() = ""
    set(value) {}

}

