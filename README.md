#Transmit
Transmit是一个基于Rxjava的Android中的的消息总线。可以用来代替Activity中的Intent传值，以及类似于onActivityResult方法中的回传值，也可以用来像在两个Fragment之间传递数据。

#Binaries
```
compile 'com.sxdsf:transmit:1.0.0'

```
注:暂时不支持此方式

#Sample usage
####1、相关概念
在Transmit中有事件Tag的概念，每次传递和发布的都是关于某个事件Tag的信息，所以说，在每个方法上都有一个`Object`类型的参数表示Tag。

该库中只有一个Transmit类，并且此类是单例，使用时直接使用即可，如下所示：
```
Transmit.getInstance().publish("test","测试");

```
####2、代替Intent传值
在Android中，一个Activity跳转到另一个Activity并传递数据，基本上是通过Intent，但是Intent只能传递比较简单和基本的类型的数据，像一些比较复杂的结构化的数据，用Intent没有办法传递，此时采用Transmit就比较简单，如下所示：
```
/*
 * 在第一个Activity跳转到第二个Activity时
 */
AActivity.this.startActivity(intent);
Transmit.getInstance().post(BActivity.class,"测试");

/************************************************/

/*
在第二个Activity中来主动收取这个事件,其中第一个参数为Tag，是前一个Activity发出事件时填写的Tag，第二个参数为这个事件的消息会被转换成的类型
 */
String msg = Transmit.getInstance().receive(BActivity.class,String.class);
```
####3、代替onActivityResult方法
在Android中，AActivity跳BActivity，如果BActivity在finish后，AActivity想接收BActivity传回来的值，那么一般会用onActivityResult方法，这种形式也是用起来比较麻烦的,此时采用Transmit就比较简单，如下所示：
```
/*
 * 在BActivity中finish时调用
 */
 BActivity.this.finish();
 Transmit.getInstance().publish("test","测试");
 
 /**********************************************/
 
 /*
  * 在AActivity中事先订阅名为“test”这个Tag的事件
  */
  Observable o = Transmit.getInstance().<String>register("test");
  o.subscribeOn(AndroidSchedulers.mainThread()).
    subscribe(new Action1<String>() {
    	@Override
        public void call(Event<String> s) {
        	Log.v("test",s);
        }
    });
    
 /*
  * 用完之后在onDestroy方法中要解除订阅
  */
  Transmit.getInstance().unRegister("test",o);
```


