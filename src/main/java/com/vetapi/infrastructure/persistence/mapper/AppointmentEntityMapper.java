package com.vetapi.infrastructure.persistence.mapper;

import com.vetapi.domain.entity.Appointment;
import com.vetapi.infrastructure.persistence.entity.AppointmentEntity;

public interface AppointmentEntityMapper {

    Appointment toAppointment (AppointmentEntity entity);
    AppointmentEntity toEntity (Appointment appointment);
}
