#!/usr/bin/env python3
"""Quick inspection of the login page structure"""
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
    page.goto(f"{BASE_URL}/login", wait_until="networkidle", timeout=30000)
    page.wait_for_timeout(2000)
    print("=== Buttons ===")
    buttons = page.locator('button').all()
    for i, btn in enumerate(buttons):
        try:
            print(f"Button {i}: text='{btn.inner_text()}' attrs={btn.evaluate('e => ({id: e.id, class: e.className, type: e.type})')}")
        except Exception as e:
            print(f"Button {i}: error {e}")
    print("\n=== Inputs ===")
    inputs = page.locator('input').all()
    for i, inp in enumerate(inputs):
        try:
            print(f"Input {i}: attrs={inp.evaluate('e => ({id: e.id, class: e.className, type: e.type, name: e.name, placeholder: e.placeholder})')}")
        except Exception as e:
            print(f"Input {i}: error {e}")
    print("\n=== Body snippet ===")
    body = page.locator('body').inner_text()
    print(body[:500])
    browser.close()
