import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        // This is the theme of your application.
        //
        // Try running your application with "flutter run". You'll see the
        // application has a blue toolbar. Then, without quitting the app, try
        // changing the primarySwatch below to Colors.green and then invoke
        // "hot reload" (press "r" in the console where you ran "flutter run",
        // or simply save your changes to "hot reload" in a Flutter IDE).
        // Notice that the counter didn't reset back to zero; the application
        // is not restarted.
        primarySwatch: Colors.blue,
      ),
      home:  MyHomePage(title: 'Flutter Demo Map Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key? key, required this.title}) : super(key: key);

  // This widget is the home page of your application. It is stateful, meaning
  // that it has a State object (defined below) that contains fields that affect
  // how it looks.

  // This class is the configuration for the state. It holds the values (in this
  // case the title) provided by the parent (in this case the App widget) and
  // used by the build method of the State. Fields in a Widget subclass are
  // always marked "final".

  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  late MethodChannel _channel;

  void onMtrcViewCreated(int id) {
    _channel = new MethodChannel('com.sensetime.mtrc_integrate/MtrcView_$id');
  }

  @override
  Widget build(BuildContext context) {
    //第一种修改状态栏字体颜色
    return Scaffold(
      appBar: AppBar(
        title: Text('data'),
        centerTitle: true,
        brightness: Brightness.light, //Brightness.dark 有appbar实现方式
      ),
      body: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              RaisedButton(
                onPressed: () async {
                  String result = await androidMethodLoginExec();
                  print(result.toString());
                },
                child: Text('regedit'),
              ),
              SizedBox(width: 5),
              RaisedButton(
                child: Text("call"),
                onPressed: () async {
                //  String result = await androidMethodCallExec();
               //   print(result.toString());
                },
              ),
              SizedBox(width: 5),
              RaisedButton(
                child: Text("handup"),
                onPressed: () async {
                  //  String result = await androidMethodCallExec();
                  //   print(result.toString());
                },
              ),
              SizedBox(width: 5),
              RaisedButton(
                child: Text("call2"),
                onPressed: () async {
                  //  String result = await androidMethodCallExec();
                  //   print(result.toString());
                },
              ),
            ],
          ),
          Container(
            width: MediaQuery.of(context).size.width,
            height: 600,
            padding: const EdgeInsets.only(
              top: 100,
              left: 0,
              right: 0,
              bottom: 20,
            ),
            child:AndroidView(
              viewType: "com.sensetime.mtrc_integrate/MtrcView",
              creationParamsCodec: const StandardMessageCodec(),
              onPlatformViewCreated: onMtrcViewCreated, //初始化
            ),
          ),
        ],

      ),
    );

    //第二种修改状态栏字体颜色
    return AnnotatedRegion<SystemUiOverlayStyle>(
        child: Container(
          color: Colors.amber,
        ),
        value: SystemUiOverlayStyle.dark //SystemUiOverlayStyle.light 两种样式
    );
  }

  Future<String> androidMethodExec() async {
    const platform = const MethodChannel("com.sensetime.mtrc_integrate/MtrcView");
    String result = "";
    try {
      result = await platform.invokeMethod('androidMethodExec',{'canshu1':'ssssss1','canshu2':'ssssss2'});
    } on Exception catch (e) {
      print(e.toString());
    }
    return result;
  }

  Future<String> androidMethodLoginExec() async {
   /* const platform = const MethodChannel("com.sensetime.mtrc_integrate/MtrcView");
    String result = "";
    try {
      result = await platform.invokeMethod('androidMethodLoginExec',{'canshu1':'s1','canshu2':'s2'});
    } on Exception catch (e) {
      print(e.toString());
    }
    return result;*/
    String result = "";
    try {
      result = await _channel.invokeMethod(
          'androidMethodLoginExec', {'canshu1': 's1', 'canshu2': 's2'});
    }on Exception catch(e){
      print(e.toString());
    }
    return  result;
  }

  Future<String> androidMethodCallExec() async {
    const platform = const MethodChannel('com.test/name');
    String result = "";
    try {
      result = await platform.invokeMethod('androidMethodCallExec',{'canshu1':'s1','canshu2':'s2'});
    } on Exception catch (e) {
      print(e.toString());
    }
    return result;
  }
}