package com.example.analyzer_manager.ui.changeip

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.analyzer_manager.databinding.FragmentChangeipBinding
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.IOException


class ChangeIpFragment : Fragment() {
    private lateinit var request: Request
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var activityContext: Context
    private var _binding: FragmentChangeipBinding? = null
    private lateinit var pref: SharedPreferences //специальный класс для сохранения и считывания данных из памяти (словарь ключ-значение)
    private val client = OkHttpClient() //клиент для отправки данных
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangeipBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //инициализируем класс, создаём таблицу MyPref в которую будем записывать ключи и значения, мод прайват - только моё приложение
        pref = requireActivity().getSharedPreferences("MyPref", MODE_PRIVATE)
        onClickApplyIp()
        getIp()
        binding.apply {
        }
        val textView: TextView = binding.textChangeip
        return root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityContext = context
    }

    private fun onClickListener(handler: Handler): View.OnClickListener {
        return View.OnClickListener {
            when(it.id){
            }
        }
    }

    //получение ip из pref
    private fun getIp() = with(binding){ // with(binding) пишем для получения доступа к editText
        val ip = pref.getString("ip", "")
        if(ip != null){
            if(ip.isNotEmpty())
                editIp.setText(ip)

        }
    }

    //нажатие на кнопку Apply и проверка на непустую строку
    private fun onClickApplyIp() = with(binding){
        bApply.setOnClickListener {
            if (editIp.text.isNotEmpty())
                applyIp(editIp.text.toString())
                val toastTextSuc = "Ip changed successfully"
                Toast.makeText(activityContext, toastTextSuc, Toast.LENGTH_SHORT).show()
        }
    }

    //сохранение адреса в таблицу pref
    private fun applyIp(ip: String){
        val editor = pref.edit()
        editor.putString("ip", ip)
        editor.apply()
    }

    //отправка данных на микроконтроллер на второстепенном потоке с помощью Thread
    fun post(post: String, handler: Handler){
        Thread{
            //указываем на какой адрес отправляем и какой запрос отправляем
            val formBody: RequestBody = FormBody.Builder()
                .add("data", post)
                .build()
            request = Request.Builder().url("http://${binding.editIp.text}:80/")
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