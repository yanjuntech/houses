#!/usr/bin/env python3
"""
测试搜索表单下拉选择"餐饮美食"后点击搜索，
验证与树点击行为一致
"""
from playwright.sync_api import sync_playwright

CHROME_PATH = "/tmp/chrome-browser/chrome-linux64/chrome"
BASE_URL = "http://localhost:8081"

with sync_playwright() as p:
    browser = p.chromium.launch(
        executable_path=CHROME_PATH,
        headless=True,
        args=["--no-sandbox", "--disable-setuid-sandbox", "--disable-dev-shm-usage"]
    )
    page = browser.new_page(viewport={"width": 1400, "height": 900})

    # Login
    page.goto(f"{BASE_URL}/login", wait_until="networkidle", timeout=30000)
    page.wait_for_timeout(2000)
    page.locator('input[type="text"]').first.fill("admin")
    page.locator('input[type="password"]').first.fill("admin123")
    page.locator('button.el-button--primary').first.click()
    page.wait_for_timeout(3000)
    try:
        page.wait_for_load_state("networkidle", timeout=15000)
    except Exception:
        pass
    print(f"登录后 URL: {page.url}")

    # Navigate to phonebook
    page.goto(f"{BASE_URL}/biz/phonebook", wait_until="networkidle", timeout=30000)
    page.wait_for_timeout(3000)
    print(f"电话簿页面 URL: {page.url}")

    # Step 1: Find the category dropdown in the search form
    print("\n=== 步骤 1: 定位搜索表单分类下拉框 ===")
    # The search form is the first el-form with inline. The category select has placeholder="请选择商家分类"
    selects = page.locator('.el-select').all()
    print(f"找到 {len(selects)} 个 el-select 下拉框")
    # Find the one with placeholder "请选择商家分类"
    target_select_idx = -1
    for i, sel in enumerate(selects):
        try:
            placeholder = sel.locator('input').first.get_attribute('placeholder')
            print(f"  下拉 {i}: placeholder='{placeholder}'")
            if placeholder and "商家分类" in placeholder:
                target_select_idx = i
        except Exception:
            pass

    if target_select_idx >= 0:
        print(f"✓ 找到分类下拉框（索引 {target_select_idx}）")
    else:
        print("✗ 未找到分类下拉框")
        browser.close()
        exit(1)

    # Step 2: Click the select to open dropdown
    print("\n=== 步骤 2: 选择'餐饮美食' ===")
    target_select = selects[target_select_idx]
    target_select.locator('input').first.click()
    page.wait_for_timeout(500)

    # Click the option "餐饮美食"
    option = page.locator('.el-select-dropdown__item:has-text("餐饮美食")').first
    if option.count() > 0:
        option.click()
        page.wait_for_timeout(500)
        print("✓ 已选择'餐饮美食'")
    else:
        print("✗ 未找到'餐饮美食'选项")
        browser.close()
        exit(1)

    # Step 3: Click the search button
    print("\n=== 步骤 3: 点击搜索按钮 ===")
    search_btn = page.locator('button:has-text("搜索")').first
    search_btn.click()
    page.wait_for_timeout(3000)
    try:
        page.wait_for_load_state("networkidle", timeout=10000)
    except Exception:
        pass
    print("✓ 点击搜索完成")

    page.screenshot(path="/workspace/.trae/specs/fix-phonebook-category-click/screenshot_search_canyin.png")
    print("✓ 截图保存: screenshot_search_canyin.png")

    # Step 4: Verify table content
    print("\n=== 步骤 4: 验证表格内容 ===")
    table_rows = page.locator('.el-table__body-wrapper .el-table__row')
    row_count = table_rows.count()
    print(f"✓ 表格行数: {row_count}")

    found_canyin = False
    for i in range(row_count):
        row = table_rows.nth(i)
        cells = row.locator('.el-table__cell')
        cell_texts = []
        for j in range(cells.count()):
            text = cells.nth(j).inner_text().strip()
            cell_texts.append(text)
        row_str = ' | '.join(cell_texts[:8])
        print(f"  行 {i+1}: {row_str}")
        if "餐饮美食" in row_str and "老北京炸酱面馆" in row_str:
            found_canyin = True

    if found_canyin and row_count == 1:
        print("\n✓ 验证通过: 搜索'餐饮美食'返回 1 行记录（老北京炸酱面馆），与树点击行为一致")
    else:
        print(f"\n✗ 验证失败: 期望 1 行餐饮美食记录，实际 {row_count} 行")

    browser.close()
