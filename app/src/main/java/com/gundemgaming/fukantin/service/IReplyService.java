package com.gundemgaming.fukantin.service;

import com.gundemgaming.fukantin.dto.reply.CreateReplyRequest;
import com.gundemgaming.fukantin.dto.reply.ReplyResponse;

import java.util.List;

public interface IReplyService {

    ReplyResponse addReply(CreateReplyRequest reply);

    List<ReplyResponse> getPostsReplies(Long postId);

    List<ReplyResponse> getUsersReplies(Long userId);

    void deleteReply(Long replyId);

}
