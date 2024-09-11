package com.example.onthetime.view

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.onthetime.R
import com.example.onthetime.databinding.FragmentMessagesBinding

class MessagesFragment : Fragment() {
lateinit var binding:FragmentMessagesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMessagesBinding.inflate(inflater)

        val textView = binding.textView
        val text = "Press the + in the menu above to start a"

        val spannableString = SpannableString(text)
        val plusIndex = text.indexOf('+')

        textView.setHighlightColor(Color.TRANSPARENT)


        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // + işaretine tıklanınca olacaklar
                Toast.makeText(widget.context, "Plus button clicked", Toast.LENGTH_SHORT).show()
            }


            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                // + işaretini farklı bir renk yapalım (örn. kırmızı)
                ds.color = ContextCompat.getColor(requireContext(), R.color.mainColor)
                ds.isUnderlineText = false
            }
        }
        spannableString.setSpan(clickableSpan, plusIndex, plusIndex + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = spannableString
        textView.movementMethod = LinkMovementMethod.getInstance()
        return binding.root
    }
}
