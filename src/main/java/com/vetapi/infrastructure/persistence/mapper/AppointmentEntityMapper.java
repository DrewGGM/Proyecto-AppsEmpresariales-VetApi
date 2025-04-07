package com.vetapi.infrastructure.persistence.mapper;

import com.vetapi.domain.entity.Appointment;
import com.vetapi.infrastructure.persistence.entity.AppointmentEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppointmentEntityMapper {

    Appointment toAppointment (AppointmentEntity entity);
    AppointmentEntity toEntity (Appointment appointment);
}
