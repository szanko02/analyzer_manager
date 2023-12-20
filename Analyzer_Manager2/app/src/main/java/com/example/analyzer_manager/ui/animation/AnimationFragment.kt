package com.example.analyzer_manager.ui.animation

import android.content.Context

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment

import com.example.analyzer_manager.R
import com.example.analyzer_manager.databinding.FragmentAnimationBinding
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.IOException


class AnimationFragment : Fragment() {
    private lateinit var activityContext: Context
    private lateinit var ip: String
    private lateinit var request: Request
    private val client = OkHttpClient() //клиент для отправки данных
    private val handler = Handler(Looper.getMainLooper())
    private var _binding: FragmentAnimationBinding? = null
    private val binding get() = _binding!!
    private lateinit var seekBarData: SeekBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnimationBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val sharedPreferences = requireActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        ip = sharedPreferences.getString("ip", "") ?: ""
        binding.apply {
            bApplySmooth.setOnClickListener(onClickListener())
            seekBarData = smoothSeekBar
            seekBarData.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    // Обновление значения TextView при изменении прогресса SeekBar
                    smoothLabel.text = "Smooth: ${(progress.toFloat())/10}"
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    // Начало отслеживания события прикосновения к SeekBar (не требуется в данном случае)
                }
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    // Окончание отслеживания события прикосновения к SeekBar (не требуется в данном случае)
                }
            })
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
                R.id.bApplySmooth -> {

                    val data = ((seekBarData.progress).toString())
                    println(data)
                    post("smooth_$data", handler)
                    val toastText = "Smooth changed to $data"
                    Toast.makeText(activityContext, toastText, Toast.LENGTH_SHORT).show()
                    }
                }
            }
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