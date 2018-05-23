package com.fong.blog.service.impl;

import com.fong.blog.domain.Vote;
import com.fong.blog.repository.VoteRepository;
import com.fong.blog.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class VoteServiceImpl implements VoteService {
    @Autowired
    private VoteRepository voteRepository;

    @Override
    public Vote getVoteById(Long id) {
        return voteRepository.findOne(id);
    }

    @Override
    @Transactional
    public void removeVote(Long id) {
        voteRepository.delete(id);
    }
}
