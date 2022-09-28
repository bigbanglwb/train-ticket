import os

PREFIX = "codewisdom"
VERSION = "1.0.0"

base_path = os.getcwd()
build_paths = []


def main():
    if not mvn_build():
        print("mvn build failed")
        exit()
    init_docker_build_paths()
    # docker_login()
    docker_build_and_push()


def mvn_build():
    mvn_status = os.system("mvn clean package -Dmaven.test.skip=true")
    return mvn_status == 0


def init_docker_build_paths():
    list_paths = os.listdir(os.getcwd())
    for p in list_paths:
        if os.path.isdir(p):
            if(p.startswith("ts-")):
                build_path=base_path + "/" + p
                build_paths.append(build_path)





def docker_build_and_push():
    name_list ["ts-ui-dashboard","ts-gateway-service","ts-avatar-service","ts-delivery-service","ts-news-service","ts-ticket-office-service","ts-voucher-service"]
    for build_path in build_paths:
        image_name = build_path.split("/")[-1]
        if image_name in name_list:
            continue
        os.chdir(build_path)
        files = os.listdir(build_path)
        if "Dockerfile" in files:
            docker_build = os.system(f"sudo docker build . -t {PREFIX}/{image_name}:{VERSION}")
            if docker_build != 0:
                print("[FAIL]" + image_name + " build failed.")
            else:
                print("[SUCCESS]" + image_name + " build success.")

#             docker_push = os.system(f"sudo docker push {PREFIX}/{image_name}:{VERSION}")
#             if docker_push != 0:
#                 print("[FAIL]" + image_name + " push failed.")
#             else:
#                 print("[SUCCESS]" + image_name + " push success.")


if __name__ == '__main__':
    main()

