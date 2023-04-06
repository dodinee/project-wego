package org.zerock.wego.service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.zerock.wego.domain.PartyDTO;
import org.zerock.wego.domain.PartyViewVO;
import org.zerock.wego.exception.ServiceException;
import org.zerock.wego.mapper.FileMapper;
import org.zerock.wego.mapper.JoinMapper;
import org.zerock.wego.mapper.PartyMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;


@Log4j2
@RequiredArgsConstructor
@Service
public class PartyService {
	
	private final PartyMapper partyMapper;
	private final JoinMapper joinMapper;
	private final FileMapper fileMapper;


	public List<PartyViewVO> getList() throws ServiceException {
		log.trace("getList() invoked.");

		try {
			return this.partyMapper.selectAll();
			
		} catch (Exception e) {
			throw new ServiceException(e);
		} // try-catch
	} // getList

	
	public Set<PartyViewVO> getRandom10List() throws ServiceException {
		log.trace("getRandom10List() invoked.");

		try {
			return this.partyMapper.selectRandom10();
		} catch (Exception e) {
			throw new ServiceException(e);
		} // try-catch
	} // getList


	// 모집글 상세 조회 
	public PartyViewVO getById(Integer partyId) throws ServiceException{
		log.trace("getById({}) invoked.", partyId);
		
		try {
			PartyViewVO party = this.partyMapper.selectById(partyId);
			Objects.requireNonNull(party);

			
			return party;

		} catch (Exception e) {
			throw new ServiceException(e);
		} // try-catch
	}// getById


	// 모집글 사진 조회
	// Base64가 필요해질 때를 대비해서 남겨둠 
//	public String getImgPathById(Integer partyId) throws ServiceException {
//		log.trace("getImgPathById({}) invoked.", partyId);
//		
//		try {
//			 
//			String path = this.partyMapper.selectPartyImgByPartyId(partyId);
//			/*
//			Resource img;
//			
//			if(path == null) {
//				
//				img = appContext.getResource("resources/img/dang.JPG");
//			}else {
//				
//				img = appContext.getResource(path);
//			}
//	        
//			@Cleanup
//			InputStream inputStream = img.getInputStream();
//			byte[] imgBytes = inputStream.readAllBytes();
//	        
//	        String base64 = Base64.getEncoder().encodeToString(imgBytes);
//		    
//			return base64;
//			*/
//			
//			if(path == null) {
//				path = "/resources/img/leaf.png";
//			}
//			
//			
//			return path;
//		}catch(Exception e) {
//			throw new ServiceException(e);
//		}// try-catch
//	}// getPartyImg
	
	
	
	public boolean register(PartyDTO dto) throws ServiceException {
		log.trace("register({}) invoked.", dto);

		try {
			return this.partyMapper.insert(dto) == 1;
		} catch (Exception e) {
			throw new ServiceException(e);
		} // try-catch
	} // register

	public boolean modify(PartyDTO dto) throws ServiceException {
		log.trace("modify({}) invoked.", dto);

		try {
			return this.partyMapper.update(dto) == 1;
		} catch (Exception e) {
			throw new ServiceException(e);
		} // try-catch
	} // modify
	
	
	
	// 모집글 삭제 
	public boolean isRemovedById(Integer partyId, Integer userId) throws ServiceException{
		log.trace("isRemovedById({}) invoked.", partyId);
		
		try {
			boolean isPartyRemoved = (this.partyMapper.deleteById(partyId) == 1);
			boolean isImgRemoved = (this.fileMapper.delete("SAN_PARTY", partyId) == 1);
			boolean isJoinRemoved = (this.joinMapper.deleteById(partyId, userId) == 1);
			
			return isPartyRemoved && isImgRemoved && isJoinRemoved; 
			
		}catch(Exception e) {
			throw new ServiceException(e);
		}// try-catch 
	}// removeParty
	
}// end class
