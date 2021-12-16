import android.content.Context
import io.flutter.plugin.platform.PlatformView
import thunder.mrtc.MrtcRender
import android.view.View
import android.util.Log
import io.flutter.plugin.common.BinaryMessenger;

class  MtrcView(context: Context, messenger: BinaryMessenger, viewId: Int, args: Map<String, Any>?): PlatformView {
    lateinit var mtrcView : MrtcRender

    init {
        Log.d("zhoud","MapView init")
        this.mtrcView = MrtcRender(context)
    }

    override fun getView(): View {
        return this.mtrcView
    }

    override fun dispose() {
      //  this.mtrcView.onDestroy()
    }
}
