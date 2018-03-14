#!/usr/bin/env bash

VCELL_ROOTDIR="$(cd "$(dirname "$0")"; cd ..; pwd)"

shopt -s -o nounset

if [ "$#" -ne 6 ]; then
    echo "usage: config.sh SITE (REPO/NAMESPACE | NAMESPACE) TAG VCELL_VERSION_NUMBER VCELL_BUILD_NUMBER OUTPUTFILE"
    exit -1
fi

_site=$1
_repo=$2
_tag=$3
_version_number=$4
_build_number=$5
_outputfile=$6

_site_lower=`echo $_site | tr '[:upper:]' '[:lower:]'`
_site_upper=`echo $_site | tr '[:lower:]' '[:upper:]'`
_site_camel="${_site_upper:0:1}${_site_lower:1:100}"


VCELL_SITE="${_site_upper}"
VCELL_SITE_CAMEL="${_site_camel}"
VCELL_REPO_NAMESPACE=$_repo
VCELL_TAG=$_tag
VCELL_VERSION_NUMBER=$_version_number
VCELL_BUILD_NUMBER=$_build_number

case $VCELL_SITE in
	REL)
		_site_port_offset=0
		_applicationId="1471-8022-1038-5553"
		;;
	BETA)
		_site_port_offset=1
		_applicationId="1471-8022-1038-5552"
		;;
	ALPHA)
		_site_port_offset=2
		_applicationId="1471-8022-1038-5554"
		;;
	TEST)
		_site_port_offset=3
		_applicationId="1471-8022-1038-5555"
		;;
	TEST2)
		_site_port_offset=4
		_applicationId="1471-8022-1038-5556"
		;;
	TEST3)
		_site_port_offset=5
		_applicationId="1471-8022-1038-5557"
		;;
	TEST4)
		_site_port_offset=6
		_applicationId="1471-8022-1038-5558"
		;;
	TEST5)
		_site_port_offset=7
		_applicationId="1471-8022-1038-5559"
		;;
	*)
		printf 'ERROR: Unknown site: %s\n' "$1" >&2
		;;
esac

VCELL_DB_URL="jdbc:oracle:thin:@VCELL-DB.cam.uchc.edu:1521/vcelldborcl.cam.uchc.edu"
VCELL_DB_DRIVER="oracle.jdbc.driver.OracleDriver"
VCELL_DB_USER="vcell"
VCELL_API_HOST_EXTERNAL=`hostname`
VCELL_JMS_HOST_EXTERNAL=`hostname`
VCELL_MONGO_HOST_EXTERNAL=`hostname`
VCELL_INSTALLER_SCP_DESTINATION=`hostname`:/Volumes/vcell/installers
VCELL_BATCH_HOST=vcell-service.cam.uchc.edu
VCELL_SLURM_CMD_SBATCH=sbatch
VCELL_SLURM_CMD_SACCT=sacct
VCELL_SLURM_CMD_SCANCEL=scancel

VCELL_API_PORT_EXTERNAL=$((8080 + $_site_port_offset))
VCELL_JMS_PORT_EXTERNAL=$((61616 + $_site_port_offset))
VCELL_JMS_RESTPORT_EXTERNAL=$((8161 + $_site_port_offset))
VCELL_MONGO_PORT_EXTERNAL=$((27017 + $_site_port_offset))
VCELL_HTC_NODELIST=
VCELL_SINGULARITY_EXTERNAL=/share/apps/vcell3/singularity
VCELL_BATCH_DOCKER_IMAGE="${VCELL_REPO_NAMESPACE}/vcell-batch:${VCELL_TAG}"
VCELL_SINGULARITY_FILENAME="${VCELL_BATCH_DOCKER_IMAGE//[\/:]/_}.img"
VCELL_SINGULARITY_IMAGE_EXTERNAL="${VCELL_SINGULARITY_EXTERNAL}/${VCELL_SINGULARITY_FILENAME}"
VCELL_SMTP_HOSTNAME=vdsmtp.cam.uchc.edu
VCELL_SMTP_PORT=25
VCELL_SMTP_EMAILADDRESS=VCell_Support@uchc.edu
VCELL_EXPORT_BASEURL=http://vcell.org/export/
VCELL_EXPORTDIR_HOST=/Volumes/vcell/export/

cat <<EOF >$_outputfile
VCELL_SITE=$VCELL_SITE
VCELL_SITE_CAMEL=$VCELL_SITE_CAMEL
VCELL_REPO_NAMESPACE=$VCELL_REPO_NAMESPACE
VCELL_TAG=$VCELL_TAG
VCELL_VERSION_NUMBER=$VCELL_VERSION_NUMBER
VCELL_BUILD_NUMBER=$VCELL_BUILD_NUMBER
VCELL_VERSION=${_site_camel}_Version_${VCELL_VERSION_NUMBER}_build_${VCELL_BUILD_NUMBER}
VCELL_API_HOST_EXTERNAL=$VCELL_API_HOST_EXTERNAL
VCELL_API_PORT_EXTERNAL=$VCELL_API_PORT_EXTERNAL
VCELL_DB_URL=$VCELL_DB_URL
VCELL_DB_DRIVER=$VCELL_DB_DRIVER
VCELL_DB_USER=$VCELL_DB_USER
VCELL_JMS_HOST_EXTERNAL=$VCELL_JMS_HOST_EXTERNAL
VCELL_JMS_PORT_EXTERNAL=$VCELL_JMS_PORT_EXTERNAL
VCELL_JMS_RESTPORT_EXTERNAL=$VCELL_JMS_RESTPORT_EXTERNAL
VCELL_MONGO_HOST_EXTERNAL=$VCELL_MONGO_HOST_EXTERNAL
VCELL_MONGO_PORT_EXTERNAL=$VCELL_MONGO_PORT_EXTERNAL
VCELL_BATCH_HOST=$VCELL_BATCH_HOST
VCELL_BATCH_USER=vcell
VCELL_SLURM_CMD_SBATCH=$VCELL_SLURM_CMD_SBATCH
VCELL_SLURM_CMD_SACCT=$VCELL_SLURM_CMD_SACCT
VCELL_SLURM_CMD_SCANCEL=$VCELL_SLURM_CMD_SCANCEL
VCELL_CLIENT_APPID=${_applicationId}
VCELL_HTCLOGS_EXTERNAL=/share/apps/vcell3/htclogs
VCELL_HTCLOGS_HOST=/Volumes/vcell/htclogs
VCELL_NATIVESOLVERDIR_EXTERNAL=/share/apps/vcell3/nativesolvers
VCELL_BATCH_DOCKER_IMAGE=$VCELL_BATCH_DOCKER_IMAGE
VCELL_SINGULARITY_FILENAME=$VCELL_SINGULARITY_FILENAME
VCELL_SINGULARITY_IMAGE_EXTERNAL=$VCELL_SINGULARITY_IMAGE_EXTERNAL
VCELL_HTC_NODELIST=$VCELL_HTC_NODELIST
VCELL_SIMDATADIR_EXTERNAL=/share/apps/vcell3/users
VCELL_SIMDATADIR_PARALLEL_EXTERNAL=/share/apps/vcell3parallel
VCELL_SIMDATADIR_HOST=/Volumes/vcell/users
VCELL_EXPORT_BASEURL=$VCELL_EXPORT_BASEURL
VCELL_EXPORTDIR_HOST=$VCELL_EXPORTDIR_HOST
VCELL_SMTP_HOSTNAME=${VCELL_SMTP_HOSTNAME}
VCELL_SMTP_PORT=${VCELL_SMTP_PORT}
VCELL_SMTP_EMAILADDRESS=${VCELL_SMTP_EMAILADDRESS}
VCELL_SECRETS_DIR=${HOME}/vcellkeys
VCELL_DEPLOY_SECRETS_DIR=${HOME}/vcellkeys
VCELL_SITE_CAMEL=${_site_camel}
VCELL_UPDATE_SITE=http://vcell.org/webstart/${_site_camel}
VCELL_BIOFORMATS_JAR_FILE=vcell-bioformats-0.0.3-SNAPSHOT-jar-with-dependencies.jar
VCELL_BIOFORMATS_JAR_URL=http://vcell.org/webstart/vcell-bioformats-0.0.3-SNAPSHOT-jar-with-dependencies.jar
VCELL_INSTALLER_JRE_MAC=macosx-amd64-1.8.0_141
VCELL_INSTALLER_JRE_WIN64=windows-amd64-1.8.0_141
VCELL_INSTALLER_JRE_WIN32=windows-x86-1.8.0_141
VCELL_INSTALLER_JRE_LINUX64=linux-amd64-1.8.0_66
VCELL_INSTALLER_JRE_LINUX32=linux-x86-1.8.0_66
VCELL_INSTALLER_JREDIR=${HOME}/.install4j6/jres
EOF

