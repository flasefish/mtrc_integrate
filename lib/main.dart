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
  late EventChannel _eventChannel;
  int count = 0;
  String _fromNativeInfo = "";

  void onMtrcViewCreated(int id) {
    _channel =  MethodChannel('com.sensetime.mtrc_integrate/MtrcView_$id');
    _eventChannel =  EventChannel('com.sensetime.mtrc_integrate/MtrcEvent_$id');
    _eventChannel.receiveBroadcastStream().listen(_onEvent, onError: _onErroe);
  }

  @override
  Widget build(BuildContext context) {
    //????????????????????????????????????
    return Scaffold(
      appBar: AppBar(
        title: Text('data'),
        centerTitle: true,
        brightness: Brightness.light, //Brightness.dark ???appbar????????????
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
                  String result = await androidMethodCallExec();
                  print(result.toString());
                },
              ),
              SizedBox(width: 5),
              RaisedButton(
                child: Text("hangup"),
                onPressed: () async {
                    String result = await androidMethodHangupExec();
                     print(result.toString());
                },
              ),
              SizedBox(width: 5),
              RaisedButton(
                child: Text("addmsg"),
                onPressed: () async {
                    String result = await androidMethodSendExec();
                     print(result.toString());
                },
              ),
            ],
          ),
          Text(_fromNativeInfo),

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
              onPlatformViewCreated: onMtrcViewCreated, //?????????
            ),
          ),
       /*   StreamBuilder(
              initialData: "????????????",
              stream: _eventChannel.receiveBroadcastStream(),
              builder: (context, snapshot) {
                return Center(
                  child: Text(
                    "eventChannel???????????????:${snapshot.data.toString()}",
                    style: TextStyle(fontSize: 15),
                  ),
                );
              }),*/
        ],

      ),
    );

    //????????????????????????????????????
    return AnnotatedRegion<SystemUiOverlayStyle>(
        child: Container(
          color: Colors.amber,
        ),
        value: SystemUiOverlayStyle.dark //SystemUiOverlayStyle.light ????????????
    );
  }

  Future<String> androidMethodExec() async {
    const platform = const MethodChannel("com.sensetime.mtrc_integrate/MtrcView");
    String result = "";
    try {
      result = await platform.invokeMethod('mtrclogin','1080');
    } on Exception catch (e) {
      print(e.toString());
    }
    return result;
  }

  Future<String> androidMethodLoginExec() async {
    String result = "";
    try {
      result = await _channel.invokeMethod(
          'MtrcLogin',  '1080');
    }on Exception catch(e){
      print(e.toString());
    }
    return  result;
  }

  Future<String> androidMethodCallExec() async {
    String result = "";
    try {
      result = await _channel.invokeMethod(
          'MtrcCall',  '1081');
    }on Exception catch(e){
      print(e.toString());
    }
    return  result;
  }

  Future<String> androidMethodHangupExec() async {
    String result = "";
    try {
      result = await _channel.invokeMethod(
          'MtrcHangup',  '');
    }on Exception catch(e){
      print(e.toString());
    }
    return  result;
  }

  Future<String> androidMethodSendExec() async {
    String result = "";
    try {
      result = await _channel.invokeMethod("addMsg", ++count);
    }on Exception catch(e){
      print(e.toString());
    }
    return  result;
  }

  /**
   * ???????????????????????????????????????eventChannel???
   */
  void _onEvent(dynamic object) {
    print(object.toString() + "-------------?????????????????????????????????");
    setState(() {
      _fromNativeInfo = object.toString();
    });
  }

  void _onErroe(Object object) {
    print(object.toString() + "-------------?????????????????????????????????");
  }
}