package com.example.analyzer_manager.ui.size

import android.content.Context

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.analyzer_manager.R
import com.example.analyzer_manager.databinding.FragmentSizeBinding
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.IOException


class SizeFragment : Fragment() {
    private lateinit var activityContext: Context
    private lateinit var ip: String
    private lateinit var request: Request
    private val client = OkHttpClient() //клиент для отправки данных
    private val handler = Handler(Looper.getMainLooper())
    private var _binding: FragmentSizeBinding? = null
    private val binding get() = _binding!!
    private lateinit var width: TextView
    private lateinit var height: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSizeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val sharedPreferences = requireActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        ip = sharedPreferences.getString("ip", "") ?: ""
        binding.apply {
            bApplyWidth.setOnClickListener(onClickListener())
            bApplyHeight.setOnClickListener(onClickListener())
        }

        width = binding.editWidth
        height = binding.editHeight
        return root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityContext = context
    }

    private fun onClickListener(): View.OnClickListener {
        return View.OnClickListener {
            when(it.id){
                R.id.bApplyWidth -> {
                    val width = width.text.toString()
                    println(width)
                    post("width_$width", handler)
                }
                R.id.bApplyHeight -> {
                    val height = height.text.toString()
                    println(height)
                    post("height_$height", handler)
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