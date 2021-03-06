package com.khanhlh.basemvvmkt.ui.home


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.database.Cursor
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.zxing.integration.android.IntentIntegrator
import com.khanhlh.basemvvmkt.R
import com.khanhlh.basemvvmkt.base.BaseFragment
import com.khanhlh.basemvvmkt.databinding.FragmentHomeBinding
import com.khanhlh.basemvvmkt.enums.UpdateType
import com.khanhlh.basemvvmkt.helper.dialog.alert
import com.khanhlh.basemvvmkt.helper.extensions.logD
import com.khanhlh.basemvvmkt.helper.recyclerview.ItemClickPresenter
import com.khanhlh.basemvvmkt.helper.recyclerview.SingleTypeAdapter
import com.khanhlh.basemvvmkt.model.Room
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(),
    ItemClickPresenter<Room> {
    private val RESULT_LOAD_IMAGE = 1
    lateinit var imageView: ImageView
    lateinit var txtInputDevice: EditText
    lateinit var txtLabel: EditText

    private val mAdapter by lazy {
        SingleTypeAdapter<Room>(mContext, R.layout.item_room, vm.rooms).apply {
            itemPresenter = this@HomeFragment
        }
    }

    override fun initView() {
        vm = HomeViewModel()
        mBinding.viewModel = vm

        initRecycler()
        observer()
        fab.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //doSomething
            }
        }
    }

    private fun observer() {
        vm.getAllRooms()
    }

    private fun initRecycler() {
        recycler.apply {
            layoutManager = GridLayoutManager(mContext, 2)
            adapter = mAdapter
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    super.getItemOffsets(outRect, view, parent, state)
//                    outRect.top = activity?.dpToPx(R.dimen.xdp_12_0) ?: 0
                }
            })
            isPrepared = true
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_home
    override fun onItemClick(v: View?, item: Room) {
        logD(item.name)
//        val bundle = bundleOf(RoomFragment.ID_ROOM to item.id, DEVICES to item.devices)
//        navigate(R.id.roomFragment, bundle)
    }

    override fun onImageClick(v: View?) {
        imageView = v as ImageView
        val i = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        startActivityForResult(i, RESULT_LOAD_IMAGE)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(49374, resultCode, data)

        if (result != null) {
            if (result.contents != null) {
                if (requestCode == 1) {
                    txtInputDevice.setText(result.contents)
                }
            }
        }

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            val selectedImage: Uri? = data.data
            val filePathColumn =
                arrayOf(MediaStore.Images.Media.DATA)
            val cursor: Cursor = mContext.contentResolver.query(
                selectedImage!!,
                filePathColumn, null, null, null
            )!!
            cursor.moveToFirst()
            val columnIndex: Int = cursor.getColumnIndex(filePathColumn[0])
            val picturePath: String = cursor.getString(columnIndex)
            cursor.close()
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath))
        }
    }

    override fun onItemLongClick(v: View?, item: Room) {
    }

    override fun onDeleteClick(v: View?, item: Room) {
        alert(
            this,
            R.string.delete_device,
            R.string.app_name,
            R.string.cancel,
            R.string.ok,
            null,
            View.OnClickListener {
                vm.updateDevice(item.id, UpdateType.REMOVE)
                    .subscribe({ toast(R.string.delete_device_success) },
                        { toast(R.string.delete_device_fail) })
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        logD("HomeFragment::onDestroy")
    }

}
