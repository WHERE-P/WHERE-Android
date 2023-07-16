import android.content.Intent
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.where.component.MyPageActivity
import com.example.where.component.QrCodeScanActivity
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult

class QRCodeScane(private var act: QrCodeScanActivity) {
    /** QRCode Scan */
    fun startQRScan() {
        val intentIntegrator = IntentIntegrator(act)

        intentIntegrator.setPrompt("안내선 안에 QR코드를 맞추면 자동으로 인식됩니다.")
        intentIntegrator.setOrientationLocked(true)
        intentIntegrator.setBeepEnabled(false)
        activityResult.launch(intentIntegrator.createScanIntent())
        intentIntegrator.initiateScan()
    }

    /** onActivityResult */
    private val activityResult =
        act.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val intentResult: IntentResult? = IntentIntegrator.parseActivityResult(
                result.resultCode,
                result.data
            )
            if (intentResult != null) {
                // QRCode Scan 성공
                val url = intentResult.contents
                val intent = Intent(act, MyPageActivity::class.java)
                intent.putExtra("url", url)
                act.startActivity(intent)
                if (intentResult.contents != null) {
                    // QRCode Scan result 있는 경우
                    Toast.makeText(act, "출석이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    // QRCode Scan result 없는 경우
                    Toast.makeText(act, "인식된 QR-data가 없습니다.", Toast.LENGTH_SHORT).show()
                }
            } else {
                // QRCode Scan 실패
                Toast.makeText(act, "QR스캔에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
}
