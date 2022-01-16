package com.iwaniuk.todolist_auth.services;

import com.iwaniuk.todolist_auth.models.ToDoItem;
import com.iwaniuk.todolist_auth.models.ToDoPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ToDoItemService {
    ToDoItem getById(String id);
    ToDoPage getByStartDateAndEndDate(Pageable pageable, LocalDate startDate, LocalDate endDate);
    ToDoItem add(ToDoItem toDoItem);
    ToDoItem update(ToDoItem toDoItem,String id);
    void delete(String id,String userID);
    ToDoPage getToday(int page, int size);
}
