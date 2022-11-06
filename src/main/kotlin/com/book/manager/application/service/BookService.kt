package com.book.manager.application.service

import com.book.manager.domain.model.BookWithRental
import com.book.manager.domain.repository.BookRepository
import org.springframework.stereotype.Service

/**
 * BookService Class
 *
 * RepositoryによるDBへのCRUD操作を用いて、BusinessLogicを実装する。
 * Interface(XxxxRepository)を実装したClass(XxxxImpl)内にDB関連実装を閉じ込める。
 */
@Service
class BookService(
    private val bookRepository: BookRepository
) {
    /**
     * getList function
     *
     * 1. execute bookRepository.findAllWithRental() function
     */
    fun getList(): List<BookWithRental> {
        return bookRepository.findAllWithRental()
    }

    fun getDetail(bookId: Long): BookWithRental {
        return bookRepository.findWithRental(bookId) ?: throw IllegalArgumentException("存在しない書籍ID: $bookId")
    }
}