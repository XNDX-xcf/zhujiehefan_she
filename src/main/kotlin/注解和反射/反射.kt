package 注解和反射

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.*
import kotlin.reflect.jvm.isAccessible

/**
 * 反射和注解
 *
 * 在程序运行过程中动态地调用类的属性和方法
 * 字节码文件kotlin{KClass}    java{Class}
 * 1.如何获取字节码文件类型
 //1.只知道这个类名
var clz = Person::class
//2.如果已经知道某个对象了
val xw= Person()
val clz2=xw.javaClass.kotlin
 * */
fun classInfo(){
    /*
   //1.只知道这个类名
   var clz = Person::class
   //2.如果已经知道某个对象了
   val xw= Person()
   val clz2=xw.javaClass.kotlin

   //可以通过class对象获取类的详细信息
   clz.simpleName.also {
       println(it)
   } //类名
   clz.qualifiedName.also {
       println(it)
   }// 全类名 有详细包路径 main.反射和注解.Person
   println(clz.supertypes)  //父类 java->Object  Kotlin->Any
    */

    //获取类的属性
    var clz = Person::class
    //declaredMemberProperties只获取当前类的属性
    clz.declaredMemberProperties.forEach { println(it)}
    //memberProperties在获取当前类属性同时获取父类地属性
    clz.memberProperties.forEach { println(it) }

    //获取方法
    //1.获取构造方法
    clz.primaryConstructor.also { println(it) }
    //获取所有的构造函数
    clz.constructors.forEach { println(it) }
    //获取其他函数
    //获取当前类的方法
    //clz.declaredFunctions.forEach { println(it) }
    //获取当前类和父类的方法
    clz.memberFunctions.forEach { println(it) }
    clz.functions.forEach { println(it) }
}

fun main() {
    //获取类的的类对象KClass
    //将Any类型转化为Person类型
   //val xw= createObj(Person::class) as Person
   // println(xw.name)

    //invokeFun(Person::class,"show")

    invokeProperty(Person::class,"age")
}

//调用属性
fun invokeProperty(clz:KClass<out Any>,propName:String){
    //创建对象
    val primary=clz.primaryConstructor
    val obj=primary?.call("Avicii")

    //查找age属性
    clz.memberProperties.find {
        it.name==propName
    }.also {
        if (it != null) {
            it.isAccessible=true
        }
        //调用对象的get方法
        //println("获得属性${propName}的值:${it?.call(obj)}")

        //调用对象的set方法
        //将KProperty类型转换为KMutableProperty类型
        //setValue(value:String):Unit  KMutableProperty1表示函数只有一个参数
        //KMutableProperty1<T,R>

        //如果属性的修饰符为private 需要设置isAccessiable为true
        val mpro=it as KMutableProperty1<Any,Any>
        //obj.setName("jack")
        mpro.set(obj!!,25)
        it.call(obj).also {
            println(it)
        }
    }
    //val p=obj as Person
    //println(p.age)
}

//调用函数
fun invokeFun(clz: KClass<out Any>, funName:String){
     //获取默认的主构造函数
    val priCons=clz.primaryConstructor
    //创建对象
    val obj=priCons?.call("毛泽东")
    //当调用类里面的方法时必须将对象传递出去
    //默认第一个参数就是函数的对象
    //查找这个函数是否存在
    for (func in clz.functions){
        if (func.name==funName){
            func.call(obj,"周恩来")
        }
        break
    }
}

fun createObj(clz:KClass<out Any>):Any{
    //1.使用默认无参的构造函数创建
    //使用createInstance必须提供一个午餐的构造函数
    //return clz.createInstance()

    //2.有参的构造函数
    //创建对象 是通过构造函数来创建的
    //找到有参的构造函数 -> 创建对象
    /*println(priContr?.name)
    println(priContr?.parameters)
    println(priContr?.returnType)*/
    val priContr = clz.primaryConstructor
    return priContr?.call("Avicii")!!


}




open class Father{
    var address:String="西南大学"
}
class Person(var name:String): Father() {
    private var age:Int=20
     constructor(): this("向往"){

     }
    fun show(des:String){
        println("my name is $name  desc:$des")
    }
}