#Transmit
***Transmit基于Rxjava,是一个Android中的消息总线。***
_ _ _

#Binaries
```
compile 'com.sxdsf:transmit:1.0.0'

```
注:暂时不支持此方式，请在**Download**中下载
_ _ _

#Features
- 代替Activity之间的Intent传值。
- 代替Activity之间onActivityResult方法传值。
- 用于Fragment之间传递数据。
- 针对话题（Topic）的订阅与发布(Subscribe/Publish)。

_ _ _

#Usage
####相关概念
在Transmit中有主动传递数据和订阅发布（Subscribe/Publish）两种服务，订阅发布还有同步（Sync）和异步（Async）两种方式。

######1、初始化服务
建议将服务的实例放在自定义的Application中，并且在`onCreate()`方法中初始化：
```
public static SyncTransmitService syncTransmitService = null;

@Override
public void onCreate() {
    super.onCreate();
    syncTransmitService = Transmit.create(TransmitServiceMode.SYNC);
    syncTransmitService.init();
}
```
通过在`Transmit`的`create()`方法中传入服务的模式创建一个服务实例。并且调用`init()`方法初始化。

######2、主动传递数据
在Android中，一个Activity跳转到另一个Activity并传递数据，基本上是通过Intent，但是Intent只能传递比较简单和基本的类型的数据，像一些比较复杂的结构化的数据，用Intent没有办法传递，此时采用Transmit就比较简单，如下所示：

在第一个Activity跳转到第二个Activity时，使用以下代码
```
MainActivity.this.startActivity(intent);
syncTransmitService.post(Main2Activity.destination, Message.create("测试"));
```
调用服务的`post()`方法来传递一个消息，其中第一个参数是本次消息传输的目的地，第二个参数是消息的实例，通过`Message`类的`create()`方法创建一个消息，方法中的参数是消息的实体，可以为任意类型。

在第二个Activity中来主动收取这个事件,其中参数为Destination，是前一个Activity发出事件时填写的。
```
String msg = syncTransmitService.receive(Main2Activity.destination, String.class);
```
`receive()`方法的返回值就是消息的实体。接收的时候，服务会默认执行一个类型检查的过滤器，如果没通过过滤器会返回null。

######3、订阅和发布（Subscribe/Publish）
我们可以通过以下代码来创建一个订阅者，表示它对某一个话题（Topic）感兴趣
```
Observable<String> observable = syncTransmitService.register(new Topic("测试"),String.class);
```
可以看出通过调用注册方法可以得到一个`Observable`的实例，这个类是RxJava中的，所以在这之后可以使用RxJava中支持的所有方法
```
observable.
	subscribeOn(Schedulers.io()).
    observeOn(AndroidSchedulers.mainThread()).
    subscribe(new Action1<String>() {
    	@Override
    	public void call(String s) {
    		System.out.println(s);
    	}
    });
```
注册好之后，其他模块想要发送此话题（Topic）的消息只需要调用
```
MessageProducer producer = syncTransmitService.createSyncProducer(new Topic("测试"));
producer.send(Message.create("测试"));
```
首先需要获得一个针对于某个话题（Topic）的生产者，之后调用`send()`方法发送消息即可。

在注册的时候还有两个方法是
```
register(Topic topic, Class<T> cls, Filter filter);
register(Topic topic, Class<T> cls, List<Filter> filterList);
```
其中Filter为过滤器，如果在注册时传入了过滤器，那么当消息分发的时候就会通过过滤器过滤，通过了才发送，没通过不会发送，如果注册时没有传入过滤器，则会默认传入一个检查消息实体类型的过滤器。

在Android中，MainActivity跳Main2Activity，如果Main2Activity在finish后，MainActivity想接收Main2Activity传回来的值，那么一般会用onActivityResult方法，这种形式也是用起来比较麻烦的,此时采用Transmit就比较简单，但是切记在Activity的`onDestroy()`方法中解除注册
```
syncTransmitService.unRegister(new Topic("测试"), observable);
```

#Download
latest library: [transmit-1.0.0.jar](https://github.com/SxdsF/Transmit/blob/master/library/transmit-1.0.0.jar)
