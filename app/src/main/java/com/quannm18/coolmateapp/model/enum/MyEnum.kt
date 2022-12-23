package com.quannm18.coolmateapp.model.enum

class MyEnum {
    class ShippingStatus{
        companion object{
            val CHUATHANHTOAN = ("Chưa thanh toán")
            val DATHANHTOAN = ("Đã thanh toán")
            val CHOXACNHAN = ("Chờ xác nhận")
            val BIHUY = ("Bị hủy")
            val DANGCHUANBI = ("Đang chuẩn bị hàng")
            val DANGVANCHUYEN = ("Đang vận chuyển")
            val DAGIAO = ("Đã giao hàng")
            val DATRA = ("Đã trả hàng")
            val DANHAN = ("Đã nhận")
        }
    }
    class LocationEnum {
        companion object{
            val QuanHuyen = "Quận/ Huyện: "
            val XaPhuong = "Xã/ Phường: "
            val TinhTP = "Tỉnh/ Thành phố: "
        }
    }

    enum class PaymentMethodEnum {
        COD, ATM
    }
}