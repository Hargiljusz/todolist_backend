package com.iwaniuk.todolist_auth.services;

import com.iwaniuk.todolist_auth.exceptions.PermissionDenied;
import com.iwaniuk.todolist_auth.exceptions.ToDoItemNotFound;
import com.iwaniuk.todolist_auth.exceptions.UserNotFound;
import com.iwaniuk.todolist_auth.models.ToDoItem;
import com.iwaniuk.todolist_auth.models.ToDoPage;
import com.iwaniuk.todolist_auth.repositories.ToDoItemRepository;
import com.iwaniuk.todolist_auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

@Service
public class ToDoItemServiceImp implements ToDoItemService{

    private final ToDoItemRepository toDoItemRepository;
    private final UserRepository userRepository;

    @Autowired
    public ToDoItemServiceImp(ToDoItemRepository toDoItemRepository, UserRepository userRepository, MongoTemplate mongoTemplate) {
        this.toDoItemRepository = toDoItemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ToDoItem getById(String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var user = userRepository.findByEmail(authentication.getName()).orElseThrow(UserNotFound::new);
        var toDoItem = toDoItemRepository.findById(id).orElseThrow(ToDoItemNotFound::new);
        if(user.getRoles().stream().anyMatch(r->r.getRole().equals("ROLE_ADMIN")) || user.getId().equals(toDoItem.getUserId())){
            return toDoItem;
        }
        throw new PermissionDenied();
    }

    @Override
    public ToDoPage getByStartDateAndEndDate(Pageable pageable, LocalDate startDate, LocalDate endDate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var user = userRepository.findByEmail(authentication.getName()).orElseThrow(UserNotFound::new);
        var list = toDoItemRepository.getDateRange(startDate,endDate,user.getId(),pageable);

        var nextPage = PageRequest.of(pageable.getPageNumber()+1, pageable.getPageSize());
        var isNext = toDoItemRepository.getDateRange(startDate,endDate,user.getId(),nextPage).size() > 0;
        return new ToDoPage(list, pageable.getPageNumber(), pageable.getPageSize(), isNext);
    }

    @Override
    public ToDoItem add(ToDoItem toDoItem) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var user = userRepository.findByEmail(authentication.getName()).orElseThrow(UserNotFound::new);
        toDoItem.setUserId(user.getId());
        return toDoItemRepository.save(toDoItem);
    }

    @Override
    public ToDoItem update(ToDoItem toDoItem,String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var user = userRepository.findByEmail(authentication.getName()).orElseThrow(UserNotFound::new);
        if(user.getRoles().stream().anyMatch(r->r.getRole().equals("ROLE_ADMIN")) || user.getId().equals(toDoItem.getUserId())){
            return toDoItemRepository.save(toDoItem);
        }
        throw new PermissionDenied();
    }

    @Override
    public void delete(String id,String userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var user = userRepository.findByEmail(authentication.getName()).orElseThrow(UserNotFound::new);
        if(user.getRoles().stream().anyMatch(r->r.getRole().equals("ROLE_ADMIN")) || user.getId().equals(userId)){
            toDoItemRepository.deleteById(id);
        }
        throw new PermissionDenied();
    }

    public ToDoPage getToday(int page, int size){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var user = userRepository.findByEmail(authentication.getName()).orElseThrow(UserNotFound::new);
        var today = LocalDate.now();
        var userId = user.getId();

        Pageable pageableRequest = PageRequest.of(page, size);
        Pageable nextPage = PageRequest.of(page+1, size);

        var result = toDoItemRepository.getToday(today,userId,pageableRequest);
        boolean isNext = toDoItemRepository.getToday(today,userId,nextPage).size() > 0;
        return new ToDoPage(result,page,size,isNext);
    }
}
