# Spring Boot와 PostGIS를 활용한 학습용 프로젝트

# 데이터 수집 및 처리 (Spring Boot)
* 실시간 위치 수신 (Data Ingestion):
외부 API(OpenSky 등)나 시뮬레이터로부터 1초 단위로 비행기 좌표(위도, 경도, 고도, 속도)를 수신.
* 최신 위치 갱신 (Real-time Upsert):
current_aircrafts 테이블 관리.
이미 존재하는 비행기(ID 기준)라면 위치만 UPDATE, 없으면 INSERT. (지도에는 항상 최신 위치만 표시하기 위함)
* 이력 데이터 적재 (Trajectory Logging):
aircraft_histories 테이블 관리.
분석 및 경로 재생을 위해 1분 주기 혹은 일정 거리 이상 이동 시 로그 저장.
* 데이터 수명 관리 (Retention Policy):
스케줄러를 통해 24시간이 지난 데이터는 histories 테이블에서 자동 삭제 (DB 용량 관리).

# 데이터 서빙 (GeoServer)
* WMS/Vector Tile 발행:
PostGIS의 current_aircrafts 테이블을 읽어 실시간 위치를 레이어로 발행.
비행기 아이콘 스타일링 적용 (회전 각도 heading 컬럼 반영).
* WFS (Web Feature Service):
클라이언트가 클릭 시 해당 비행기의 상세 정보(속도, 항공사 등)를 JSON으로 반환.