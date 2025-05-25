package com.cretf.backend.product.service.impl;

import com.cretf.backend.common.jdbc_service.BaseJdbcServiceImpl;
import com.cretf.backend.product.dto.PropertyCommentDTO;
import com.cretf.backend.product.entity.PropertyComment;
import com.cretf.backend.product.repository.PropertyCommentRepository;
import com.cretf.backend.product.service.PropertyCommentService;
import jakarta.persistence.EntityManager;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PropertyCommentServiceImpl extends BaseJdbcServiceImpl<PropertyCommentDTO, String> implements PropertyCommentService {
    private final String FILE_EXTENSION = "sql";
    private final String FILE_PATH_NAME = "sqlScripts/Location";

    private final ModelMapper modelMapper;
    private final PropertyCommentRepository propertyCommentRepository;

    public PropertyCommentServiceImpl(
            EntityManager entityManager,
            ModelMapper modelMapper,
            PropertyCommentRepository propertyCommentRepository
    ) {
        super(entityManager, PropertyCommentDTO.class);
        this.modelMapper = modelMapper;
        this.propertyCommentRepository = propertyCommentRepository;
    }

    @Override
    public List<PropertyCommentDTO> getPropertyCommentByPropertyId(String propertyId) throws Exception {
        List<PropertyComment> propertyComments = propertyCommentRepository.findByPropertyId(propertyId);
        List<PropertyCommentDTO> propertyCommentDTOs = propertyComments.stream().map(item -> {
            return modelMapper.map(item, PropertyCommentDTO.class);
        }).collect(Collectors.toList());

        return propertyCommentDTOs;
    }
    private String generatePropertyCommentCode(String parentCode) {
        String newCode = UUID.randomUUID().toString().substring(0, 4);

        if (parentCode == null || parentCode.isEmpty()) {
            return newCode;
        } else {
            return parentCode + "-" + newCode;
        }
    }

    @Override
    public PropertyCommentDTO create(PropertyCommentDTO propertyCommentDTO) throws Exception {
        String code = generatePropertyCommentCode(propertyCommentDTO.getParentCode());
        propertyCommentDTO.setCode(code);

        if (propertyCommentDTO.getParentCode() != null && !propertyCommentDTO.getParentCode().isEmpty()) {
            Optional<PropertyComment> parentComment = propertyCommentRepository.findByCode(propertyCommentDTO.getParentCode());
            if (parentComment.isPresent()) {
                Integer parentLevel = parentComment.get().getLevel();
                propertyCommentDTO.setLevel(parentLevel + 1);
            } else {
                propertyCommentDTO.setLevel(1);
            }
        } else {
            propertyCommentDTO.setLevel(1);
            propertyCommentDTO.setPath(code);
        }
        propertyCommentDTO.setDateCreated(new Date());

        PropertyComment propertyComment = modelMapper.map(propertyCommentDTO, PropertyComment.class);
        propertyCommentRepository.save(propertyComment);
        return modelMapper.map(propertyComment, PropertyCommentDTO.class);
    }

    @Override
    public boolean delete(String propertyCommentId) throws Exception {
        Optional<PropertyComment> propertyCommentOptional = propertyCommentRepository.findById(propertyCommentId);
        if(propertyCommentOptional.isPresent()){
            propertyCommentRepository.delete(propertyCommentOptional.get());
            return true;
        }
        return false;
    }
}
