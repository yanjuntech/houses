- [x] Checkpoint 1: `BizHouseController` 新增 `GET /rental/house/miniapp/list` 公开接口（@Anonymous），返回分页房屋列表，默认仅上架状态
- [x] Checkpoint 2: `BizHouseController` 新增 `GET /rental/house/miniapp/{houseId}` 公开接口（@Anonymous），返回房屋详情
- [x] Checkpoint 3: `BizHouseController` 新增 `POST /rental/house/miniapp/publish` 公开接口（@Anonymous），扣减发布配额并创建房屋
- [x] Checkpoint 4: `BizHouseRepairController` 新增 `GET /rental/repair/miniapp/list` 公开接口（@Anonymous），按 userId 筛选维修列表
- [x] Checkpoint 5: `BizInviteRelationController` 新增 `GET /rental/invite/miniapp/statistics/{inviterId}` 公开接口（@Anonymous）
- [x] Checkpoint 6: `BizInviteRelationController` 新增 `GET /rental/invite/miniapp/inviteList/{inviterId}` 公开接口（@Anonymous）
- [x] Checkpoint 7: `BizMallExchangeRecordController` 的 `GET /rental/mallRecord/userRecord/{userId}` 接口添加 @Anonymous
- [x] Checkpoint 8: `BizExchangeQuotaController` 的 `GET /rental/exchangeQuota/userQuota/{userId}` 接口添加 @Anonymous
- [x] Checkpoint 9: `BizUserFavoriteController` 的 `GET /rental/favorite/userFavorite/{userId}` 接口添加 @Anonymous
- [x] Checkpoint 10: `BizUserBrowseController` 的 `GET /rental/browse/userBrowse/{userId}` 接口添加 @Anonymous
- [x] Checkpoint 11: 所有公开接口路径与前端 api.js 定义一致
- [x] Checkpoint 12: 所有公开接口返回格式正确（AjaxResult 或 TableDataInfo）

---

## 验证总结

- **通过检查点**：12 / 12
- **未通过检查点**：0 / 12
- **状态**：全部通过 ✅
