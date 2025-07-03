package com.bancolombia.ms_stats.infraestructure.bus.repository;

import com.bancolombia.ms_stats.domain.model.Stats;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class StatsData implements Serializable {

    Stats stats;
}
