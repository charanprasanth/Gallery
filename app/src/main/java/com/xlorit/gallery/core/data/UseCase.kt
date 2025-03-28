package com.xlorit.gallery.core.data

abstract class UseCase<in Param, out Result> {
    abstract suspend operator fun invoke(params: Param): Result
}
