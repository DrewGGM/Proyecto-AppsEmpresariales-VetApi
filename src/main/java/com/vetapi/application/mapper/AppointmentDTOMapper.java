package com.vetapi.application.mapper;

import com.vetapi.application.dto.appointment.AppointmentCreateDTO;
import com.vetapi.application.dto.appointment.AppointmentDTO;
import com.vetapi.application.dto.appointment.AppointmentListDTO;
import com.vetapi.application.dto.appointment.AppointmentUpdateDTO;
import com.vetapi.domain.entity.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;


@Mapper(componentModel = "spring")
public interface AppointmentDTOMapper {


    @Mapping(target = "petName",source = "pet.name")
    @Mapping(target = "petId", source = "pet.id")
    @Mapping(target = "veterinarianName", source = "veterinarian.name")
    @Mapping(target = "veterinarianId", source = "veterinarian.id")
    @Mapping(target = "isToday",expression = "java(appointment.isToday())")
    @Mapping(target = "isPending",expression = "java(appointment.isPending())")
    AppointmentDTO toAppointmentDTO(Appointment appointment);

    @Mapping(target = "petName",source = "pet.name")
    @Mapping(target = "veterinarianName", source = "veterinarian.name")
    @Mapping(target = "isToday",expression = "java(appointment.isToday())")
    AppointmentListDTO toAppointmentListDTO(Appointment appointment);

    List<AppointmentDTO> toAppointmentDTOList(List<Appointment> Appointments);
    List<AppointmentListDTO> toAppointmentListDTOList(List<Appointment> appointments);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status",  ignore = true)
    @Mapping(target = "confirmed", constant = "true")
    @Mapping(target = "pet", ignore = true)
    @Mapping(target = "veterinarian", ignore = true)
    @Mapping(target = "active", constant = "true")
    Appointment toAppointment(AppointmentCreateDTO createDTO);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "pet", ignore = true)
    @Mapping(target = "veterinarian", ignore = true)
    @Mapping(target = "active", ignore = true)
    void updateAppointmentFromDTO(AppointmentUpdateDTO updateDTO, @MappingTarget Appointment appointment);


}
