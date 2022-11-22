package SpringAPIStudy.bookstore.app.item.service.impl;

import SpringAPIStudy.bookstore.app.item.entity.ItemImg;
import SpringAPIStudy.bookstore.app.item.repository.ItemImgRepository;
import SpringAPIStudy.bookstore.app.item.service.FileService;
import SpringAPIStudy.bookstore.app.item.service.ItemImgService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.NoSuchElementException;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ItemImgServiceImpl implements ItemImgService {

    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;

    private final FileService fileService;

    @Override
    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        //파일 업로드
        if(!StringUtils.isEmpty(oriImgName)){
            imgName = fileService.uploadFile(itemImgLocation, oriImgName,
                    itemImgFile.getBytes());
            imgUrl = "/images/item/" + imgName;
        }

        //상품 이미지 정보 저장
        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);
    }

    @Override
    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception {
        if(!itemImgFile.isEmpty()){
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId)
                    .orElseThrow(NoSuchElementException::new);

            //기존 이미지 파일 삭제
            if(!StringUtils.isEmpty(savedItemImg.getImgName())) {
                fileService.deleteFile(itemImgLocation+"/"+
                        savedItemImg.getImgName());
            }

            String oriImgName = itemImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            String imgUrl = "/images/item/" + imgName;
            savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
        }
    }
}
