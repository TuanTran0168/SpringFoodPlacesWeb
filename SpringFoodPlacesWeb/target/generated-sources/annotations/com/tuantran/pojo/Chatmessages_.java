package com.tuantran.pojo;

import com.tuantran.pojo.Users;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.10.v20211216-rNA", date="2023-07-30T17:04:53")
@StaticMetamodel(Chatmessages.class)
public class Chatmessages_ { 

    public static volatile SingularAttribute<Chatmessages, Users> senderId;
    public static volatile SingularAttribute<Chatmessages, Users> receiverId;
    public static volatile SingularAttribute<Chatmessages, Integer> messageId;
    public static volatile SingularAttribute<Chatmessages, Boolean> active;
    public static volatile SingularAttribute<Chatmessages, String> messageContent;
    public static volatile SingularAttribute<Chatmessages, Date> timestamp;

}