# Remoteseminar 자동화



## Test 자동화 구현
* Selenium + TestNG
  * 가장 대중화 되고 보편적인 Web Test
  * Selenium : 간단한 스크립트 형식으로 테스트 케이스 작성 가능
  * TestNG : xml로 기능에 대한 테스트 반복 및 재활용 가능, 테스트 결과 Report 취합(HTML 등..) 다른 툴에서 플러그인으로 활용 가능
  
 
 
 ## 자동화 구현 예시
 * Java로 작성
 * 테스트는 함수 단위
 * class 상단에 케이스(함수 단위) 나열 및 설명(유지보수 목적)
 * 각 함수에서 케이스 구현 (UI 확인 가능한 케이스)

  
## Jenkins 연동

![이미지 1](https://user-images.githubusercontent.com/50477361/93306633-1ce8d000-f83b-11ea-8098-eed6c9b5fcd9.png)

* Alpha 서버 배포시 트리거하여 Test 자동실행(Curl 사용)
