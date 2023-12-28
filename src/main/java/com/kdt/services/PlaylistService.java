package com.kdt.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kdt.domain.entity.Playlist;
import com.kdt.domain.entity.PlaylistTrack;
import com.kdt.dto.PlaylistDTO;
import com.kdt.mappers.PlaylistMapper;
import com.kdt.mappers.PlaylistTrackMapper;
import com.kdt.repositories.PlaylistRepository;
import com.kdt.repositories.PlaylistTrackRepository;

@Service
public class PlaylistService {

	@Autowired
	private PlaylistRepository plRepo;
	
	@Autowired
	private PlaylistTrackRepository pltRepo;
	
	@Autowired
	private PlaylistMapper plMapper;
	
	@Autowired
	private PlaylistTrackMapper pltMapper;
	
	@Transactional
	public void insert(PlaylistDTO dto) {
		Playlist pl = plMapper.toEntity(dto);
		List<PlaylistTrack> playlistTracks = pl.getPlaylistTracks();
		for (PlaylistTrack track : playlistTracks) {
			track.setPlaylist(pl);
		}
		plRepo.save(pl);
	}
	
	@Transactional
	public void insertPlaylist(PlaylistDTO dto) {
		Playlist playlist = plMapper.toEntity(dto);

	    // Set the playlist reference for each track
	    List<PlaylistTrack> playlistTracks = playlist.getPlaylistTracks();
	    for (PlaylistTrack track : playlistTracks) {
	        track.setPlaylist(playlist);
	    }
	    plRepo.save(playlist);
	}
	
	@Transactional
    public void updatePlaylist(Long playlistSeq, String title, String visibility) {
        Playlist playlist = plRepo.findById(playlistSeq)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        playlist.setPlaylistPlTitle(title);
        playlist.setPlaylistVisibility(visibility);
        plRepo.save(playlist);
    }
	
	@Transactional
	public List<PlaylistDTO> selectAll(String id) {
		List<Playlist> list = plRepo.findByPlaylistWriteId(id);
		List<PlaylistDTO> dtoList = plMapper.toDtoList(list);
		return dtoList;
	}
	
	@Transactional
	public void deletePlaylist(Long playlistSeq) {
		plRepo.deletePlaylistTracksByPlaylistParentSeq(playlistSeq);
		plRepo.deleteById(playlistSeq);
	}
	
	@Transactional
	public void deleteTrack(Long playlistSeq) {
		pltRepo.deleteById(playlistSeq);
	}
}
