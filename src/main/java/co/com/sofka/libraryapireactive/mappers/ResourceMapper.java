package co.com.sofka.libraryapireactive.mappers;

import co.com.sofka.libraryapireactive.dtos.ResourceDTO;
import co.com.sofka.libraryapireactive.collections.Resource;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Component
public class ResourceMapper {

    private  ModelMapper modelMapper;
    private  DateTimeFormatter dateTimeFormatter;

    @Autowired
    public ResourceMapper(ModelMapper modelMapper, DateTimeFormatter dateTimeFormatter) {
        this.modelMapper = modelMapper;
        this.dateTimeFormatter = dateTimeFormatter;
    }

    public ResourceDTO fromEntity(Resource resource)
    {
        ResourceDTO resourceDTO = modelMapper.map(resource, ResourceDTO.class);
        if (resource.getDateLent() != null)
            resourceDTO.setDateLent(dateTimeFormatter.format(resource.getDateLent()));

        return resourceDTO;
    }
    public Resource fromDTO(ResourceDTO resourceDTO)
    {
        Resource resource = modelMapper.map(resourceDTO, Resource.class);
        if (resourceDTO.getDateLent() != null)
            resource.setDateLent(LocalDate.parse(resourceDTO.getDateLent(), dateTimeFormatter));

        return resource;
    }

}