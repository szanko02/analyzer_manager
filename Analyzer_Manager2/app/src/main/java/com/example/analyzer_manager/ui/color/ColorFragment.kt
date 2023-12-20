package com.example.analyzer_manager.ui.color

import android.content.Context

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

import com.example.analyzer_manager.R
import com.example.analyzer_manager.databinding.FragmentColorBinding
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.IOException


class ColorFragment : Fragment() {
    private lateinit var activityContext: Context
    private lateinit var ip: String
    private lateinit var request: Request
    private val client = OkHttpClient() //клиент для отправки данных
    private val handler = Handler(Looper.getMainLooper())
    private var _binding: FragmentColorBinding? = null
    private val binding get() = _binding!!

    private lateinit var seekBarR: SeekBar
    private lateinit var seekBarG: SeekBar
    private lateinit var seekBarB: SeekBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentColorBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val sharedPreferences = requireActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        ip = sharedPreferences.getString("ip", "") ?: ""
        binding.apply {
            bApplyColor1.setOnClickListener(onClickListener())
            bApplyColor2.setOnClickListener(onClickListener())
            bApplyColor3.setOnClickListener(onClickListener())
            bApplyColor4.setOnClickListener(onClickListener())
            seekBarR = RSeekBar
            seekBarG = GSeekBar
            seekBarB = BSeekBar
        }


        return root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityContext = context
    }

    private fun onClickListener(): View.OnClickListener {
        return View.OnClickListener {
            when(it.id){
                R.id.bApplyColor1 -> {
                    setColor("color1")
                    val toastText = "Color 1 changed"
                    Toast.makeText(activityContext, toastText, Toast.LENGTH_SHORT).show()
                    }
                R.id.bApplyColor2 -> {
                    setColor("color2")
                    val toastText = "Color 2 changed"
                    Toast.makeText(activityContext, toastText, Toast.LENGTH_SHORT).show()
                }
                R.id.bApplyColor3 -> {
                    setColor("color3")
                    val toastText = "Color 3 changed"
                    Toast.makeText(activityContext, toastText, Toast.LENGTH_SHORT).show()
                }
                R.id.bApplyColor4 -> {
                    setColor("color4")
                    val toastText = "Color 4 changed"
                    Toast.makeText(activityContext, toastText, Toast.LENGTH_SHORT).show()
                }
                }
            }
        }

    fun setColor(colorName: String){
        val req: String

        req = colorName + "_" + seekBarR.progress + "_" + seekBarG.progress + "_" + seekBarB.progress
        post(req, handler)
    }
    fun post(post: String, handler: Handler){
        Thread{
            //указываем на какой адрес отправляем и какой запрос отправляем
            val formBody: RequestBody = FormBody.Builder()
                .add("data", post)
                .build()
            request = Request.Builder().url("http://$ip:80/")
                .post(formBody)
                .build()
            try{
                //отправляет синхронный HTTP-запрос с использованием OkHttpClient и получает ответ в переменную response
                val response = client.newCall(request).execute()
                println("asdhjioasdijasdjop")
                println(response.body())
                //получаем данные
            } catch (i: IOException){
                i.printStackTrace()
                handler.post {
                    val toastText = "Error while sending request"
                    Toast.makeText(activityContext, toastText, Toast.LENGTH_SHORT).show()}
            }
        }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}