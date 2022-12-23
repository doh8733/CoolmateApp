package com.quannm18.coolmateapp.model.filter

class CloneDataFilter {
    fun getAllStyle(): MutableList<String> {
        val mListStyle = mutableListOf<String>()
        mListStyle.add(("Tất cả"))
        mListStyle.add(("OVERSIZE"))
        mListStyle.add(("REGULAR FIT"))
        mListStyle.add(("Áo ngắn tay"))
        mListStyle.add(("Áo dài tay"))
        return mListStyle
    }

    fun getAllCatalog(): MutableList<String> {
        val catalogs = mutableListOf<String>()
        catalogs.add(("Tất cả"))
        catalogs.add(("Áo thun"))
        catalogs.add(("Áo sơ mi"))
        catalogs.add(("Áo khoác"))
        catalogs.add(("Áo polo"))
        catalogs.add(("Áo khoác nam"))
        catalogs.add(("Áo sơ mi nam"))
        catalogs.add(("Áo tank top"))
        catalogs.add(("Áo tank top nam"))
        catalogs.add(("Áo thể thao nam"))
        catalogs.add(("Áo in hình"))
        catalogs.add(("Áo khoác thể thao"))
        return catalogs
    }

    fun getAllMaterial(): MutableList<String> {
        val materials = mutableListOf<String>()
        materials.add(("Tất cả"))
        materials.add(("Cotton"))
        materials.add(("Polyester"))
        materials.add(("Recycler"))
        materials.add(("Excool"))
        materials.add(("Cafe"))
        materials.add(("Denim"))
        materials.add(("Kaki"))
        materials.add(("Bamboo"))
        return materials
    }

    fun getAllPurpose(): MutableList<String> {
        val purposes = mutableListOf<String>()
        purposes.add(("Tất cả"))
        purposes.add(("Chơi thể thao"))
        purposes.add(("Đi chơi"))
        purposes.add(("Đi làm"))
        purposes.add(("Đi du lịch"))
        purposes.add(("Đi học"))
        purposes.add(("Ở nhà"))
        return purposes
    }

    fun getAllFeature(): MutableList<String> {
        val purposes = mutableListOf<String>()
        purposes.add(("Tất cả"))
        purposes.add(("Chống nhăn"))
        purposes.add(("Chống tia UV"))
        purposes.add(("Chống nắng"))
        purposes.add(("Co dãn tốt"))
        purposes.add(("Mềm mại"))
        purposes.add(("Thoải mái"))
        purposes.add(("Nhanh khô"))
        purposes.add(("Thoáng khí"))
        purposes.add(("Thấm hút tốt"))
        return purposes
    }
}