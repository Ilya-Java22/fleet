package ru.skillsmart.fleet.service;

public interface TrackGenerationService {
    void generateTrack(Integer vehicleId, Double trackLength, String city, Integer trackStep);
}
