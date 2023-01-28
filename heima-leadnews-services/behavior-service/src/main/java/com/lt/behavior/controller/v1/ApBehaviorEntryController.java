package com.lt.behavior.controller.v1;

import com.lt.behavior.service.ApBehaviorEntryService;
import com.lt.model.behavior.pojo.ApBehaviorEntry;
import com.lt.model.common.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/28 22:41
 */
@RestController
@RequestMapping("/api/v1/behavior_entry")
public class ApBehaviorEntryController {
    @Autowired
    ApBehaviorEntryService apBehaviorEntryService;

    @GetMapping("/one")
    public ResponseResult<ApBehaviorEntry> findByUserIdOrEquipmentId(
            @RequestParam(value = "userId", required = false) Integer userId,
            @RequestParam(value = "equipmentId", required = false) Integer equipmentId) {
        ApBehaviorEntry apBehaviorEntry = apBehaviorEntryService.findByUserIdOrEquipmentId(userId, equipmentId);
        return ResponseResult.okResult(apBehaviorEntry);
    }
}
