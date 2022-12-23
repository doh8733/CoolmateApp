package com.quannm18.coolmateapp.bus

import com.quannm18.coolmateapp.utils.Status

data class ListenerValidate(
    var message: String = "",
    var status: Status = Status.ERROR
)