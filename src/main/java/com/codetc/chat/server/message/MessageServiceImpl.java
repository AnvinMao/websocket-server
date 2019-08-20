package com.codetc.chat.server.message;

import com.codetc.chat.server.message.entity.MessageEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Resource
    private MongoTemplate mongoTemplate;

    @Async
    @Override
    public void saveMessage(MessageEntity message) {
        this.mongoTemplate.save(message);
    }

    @Override
    public List<MessageEntity> getMessage(long senderId, long receiverId, int limit, int skip) {
        Query query = new Query();
        query.addCriteria(
                new Criteria().andOperator(
                        Criteria.where("senderId").is(senderId),
                        Criteria.where("receiverId").is(receiverId)));
        query.with(new Sort(Sort.Direction.DESC, "time"));
        query.limit(limit).skip(skip);

        return this.mongoTemplate.find(query, MessageEntity.class);
    }
}
