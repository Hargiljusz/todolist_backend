package com.iwaniuk.todolist_auth.repositories;

import com.iwaniuk.todolist_auth.models.ToDoItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ToDoItemRepository extends MongoRepository<ToDoItem,String> {
    Page<ToDoItem> findByStartDateAndIsOneDayAndUserId(LocalDate startDate, boolean isOneDay, String userId, Pageable pageable);

    @Aggregation("{" +
            "'$match': {" +
            "    '$and': [" +
            "      {" +
            "        'userId': {" +
            "          $eq: '?1'"+
            "        }" +
            "      }," +
            "      {" +
            "        '$and': [" +
            "          {" +
            "            'startDate': {" +
            "              '$lte': ?0 " +
            "            }" +
            "          }," +
            "          {" +
            "            'endDate': {" +
            "              '$gte':?0" +
            "            }" +
            "          }" +
            "        ]" +
            "      }" +
            "    ]" +
            "  }" +
            "}")
    List<ToDoItem> getToday(LocalDate curr, String userId, Pageable pageable);

    @Aggregation("{" +
            "  '$match': {" +
            "    '$and': [" +
            "      {" +
            "        'userId': {" +
            "          $eq: '?2'" +
            "        }" +
            "      }," +
            "      {" +
            "        '$and': [" +
            "          {" +
            "            '$or': [" +
            "              {" +
            "                '$and': [" +
            "                  {" +
            "                    'startDate': {" +
            "                      $gte: ?0" +
            "                    }" +
            "                  }," +
            "                  {" +
            "                    'endDate': {" +
            "                      $gte: ?0" +
            "                    }" +
            "                  }" +
            "                ]" +
            "              }," +
            "              {" +
            "                '$and': [" +
            "                  {" +
            "                    'startDate': {" +
            "                      $lte: ?0" +
            "                    }" +
            "                  }," +
            "                  {" +
            "                    'endDate': {" +
            "                      $gte: ?0" +
            "                    }" +
            "                  }" +
            "                ]" +
            "              }" +
            "            ]" +
            "          }," +
            "          {" +
            "            '$or': [" +
            "              {" +
            "                '$and': [" +
            "                  {" +
            "                    'startDate': {" +
            "                      $lte: ?1" +
            "                    }" +
            "                  }," +
            "                  {" +
            "                    'endDate': {" +
            "                      $lte: ?1" +
            "                    }" +
            "                  }" +
            "                ]" +
            "              }," +
            "              {" +
            "                '$and': [" +
            "                  {" +
            "                    'startDate': {" +
            "                      $lte: ?1" +
            "                    }" +
            "                  }," +
            "                  {" +
            "                    'endDate': {" +
            "                      $gte: ?1" +
            "                    }" +
            "                  }" +
            "                ]" +
            "              }" +
            "            ]" +
            "          }" +
            "        ]" +
            "      }" +
            "    ]" +
            "  }" +
            "}")
    List<ToDoItem> getDateRange(LocalDate start,LocalDate end, String userId, Pageable pageable);

}
