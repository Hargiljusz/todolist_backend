package com.iwaniuk.todolist_auth.controllers;

import com.iwaniuk.todolist_auth.models.ToDoItem;
import com.iwaniuk.todolist_auth.models.ToDoPage;
import com.iwaniuk.todolist_auth.services.ToDoItemService;
import com.iwaniuk.todolist_auth.services.ToDoItemServiceImp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@RestController()
@RequestMapping("/api/todo")
public class ToDoListController {
    private final ToDoItemService toDoItemService;

    public ToDoListController(ToDoItemService toDoItemService) {
        this.toDoItemService = toDoItemService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ToDoItem> getById(@PathVariable String id){
        return ok(toDoItemService.getById(id));
    }

    @GetMapping("/today")
    public ResponseEntity<ToDoPage> getToday(@RequestParam int page, @RequestParam int size){
        return ok(toDoItemService.getToday(page,size));
    }

    @GetMapping("/range")
    public ResponseEntity<ToDoPage> getFromStarDateToEndDate(@RequestParam int page, @RequestParam int size, @RequestParam String start, @RequestParam String end){
        var pageable = PageRequest.of(page,size);
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        return ok(toDoItemService.getByStartDateAndEndDate(pageable,startDate,endDate));
    }

    @PostMapping("/")
    public ResponseEntity<ToDoItem> create(@RequestBody ToDoItem toDoItem){
        return ok(toDoItemService.add(toDoItem));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ToDoItem> update(@PathVariable String id,@RequestBody ToDoItem toDoItem){
        return ok(toDoItemService.update(toDoItem,id));
    }

    @DeleteMapping("/")
    public ResponseEntity update(@RequestParam String id,@RequestParam String userId){
        toDoItemService.delete(id,userId);
        return noContent().build();
    }
}
