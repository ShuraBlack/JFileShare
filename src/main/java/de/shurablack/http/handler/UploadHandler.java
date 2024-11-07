package de.shurablack.http.handler;

import de.shurablack.http.Request;
import de.shurablack.http.RequestHandler;
import de.shurablack.session.Session;
import de.shurablack.util.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UploadHandler extends RequestHandler {

    private static final Logger LOGGER = LogManager.getLogger(UploadHandler.class);

    @Override
    public void post(Request request) {
        final String identifier = request.getHeader("identifier");
        if (identifier == null) {
            this.logError(request, "No session identifier", 401);
            return;
        }

        final Session session = SessionHandler.getSession(identifier);
        if (session == null) {
            this.logError(request, "Session not found", 401);
            return;
        }

        final String contentType = request.getHeader("Content-Type");
        if (contentType == null || !contentType.startsWith("multipart/form-data")) {
            this.logError(request, "Invalid content type", 400);
            return;
        }

        if (Config.isVerbose()) {
            LOGGER.info("<IP:{}> {} Request upload",
                    request.getOrigin().getRemoteAddress().getAddress(),
                    request.getMethod()
            );
        }

        try {
            List<MultiPart> parts = prepareFileUpload(contentType, request);
            handle(request, session, parts);
        } catch (IOException e) {
            this.logError(request, "Error while handling upload", 500);
        }
    }

    private ArrayList<MultiPart> prepareFileUpload(final String contentType, final Request request) {
            String boundary = contentType.substring(contentType.indexOf("boundary=") + 9);
            byte[] boundaryBytes = ("\r\n--" + boundary).getBytes(StandardCharsets.UTF_8);
            byte[] payload = getInputAsBinary(request.getOrigin().getRequestBody());
            ArrayList<MultiPart> list = new ArrayList<>();

            List<Integer> offsets = searchBytes(payload, boundaryBytes, 0, payload.length - 1);
            offsets.add(0, 0);
            for (int idx = 0 ; idx < offsets.size() ; idx++){
                int startPart = offsets.get(idx);
                int endPart = payload.length;
                if (idx<offsets.size() - 1){
                    endPart = offsets.get(idx + 1);
                }
                byte[] part = Arrays.copyOfRange(payload, startPart, endPart);

                int headerEnd = indexOf(part,"\r\n\r\n".getBytes(StandardCharsets.UTF_8), 0, part.length-1);
                if(headerEnd > 0) {
                    MultiPart p = new MultiPart();
                    byte[] head = Arrays.copyOfRange(part, 0, headerEnd);
                    String header = new String(head);

                    int nameIndex = header.indexOf("\r\nContent-Disposition: form-data; name=");
                    if (nameIndex >= 0) {

                        int fileNameStart = header.indexOf("; filename=");
                        if (fileNameStart >= 0) {
                            String filename = header.substring(fileNameStart + 11, header.indexOf("\r\n", fileNameStart));
                            p.filename = filename.replace('"', ' ').replace('\'', ' ').trim();
                        }
                    } else {
                        continue;
                    }

                    p.bytes = Arrays.copyOfRange(part, headerEnd + 4, part.length);
                    list.add(p);
                }
            }

            return list;
    }

    public void handle(Request request, final Session session, List<MultiPart> parts) throws IOException {
        if (parts != null && !parts.isEmpty()) {
            for (MultiPart part : parts) {
                try (FileOutputStream stream = new FileOutputStream(session.getCurrentDirectory() + "/" + part.filename)) {
                    stream.write(part.bytes);
                }
            }
        }
        request.sendEmptyResponse(200);
    }

    public static byte[] getInputAsBinary(InputStream requestStream) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            byte[] buf = new byte[100000];
            int bytesRead=0;
            while ((bytesRead = requestStream.read(buf)) != -1){
                bos.write(buf, 0, bytesRead);
            }
            requestStream.close();
            bos.close();
        } catch (IOException e) {
            LOGGER.error("Error while decoding http input stream", e);
        }
        return bos.toByteArray();
    }

    public List<Integer> searchBytes(byte[] srcBytes, byte[] searchBytes, int searchStartIndex, int searchEndIndex) {
        final int destSize = searchBytes.length;
        final List<Integer> positionIndexList = new ArrayList<>();
        int cursor = searchStartIndex;
        while (cursor < searchEndIndex + 1) {
            int index = indexOf(srcBytes, searchBytes, cursor, searchEndIndex);
            if (index >= 0) {
                positionIndexList.add(index);
                cursor = index + destSize;
            } else {
                cursor++;
            }
        }
        return positionIndexList;
    }

    public int indexOf(byte[] srcBytes, byte[] searchBytes, int startIndex, int endIndex) {
        if (searchBytes.length == 0 || (endIndex - startIndex + 1) < searchBytes.length) {
            return -1;
        }
        int maxScanStartPosIdx = srcBytes.length - searchBytes.length;
        final int loopEndIdx;
        if (endIndex < maxScanStartPosIdx) {
            loopEndIdx = endIndex;
        } else {
            loopEndIdx = maxScanStartPosIdx;
        }
        int lastScanIdx = -1;
        label:
        for (int i = startIndex; i <= loopEndIdx; i++) {
            for (int j = 0; j < searchBytes.length; j++) {
                if (srcBytes[i + j] != searchBytes[j]) {
                    continue label;
                }
                lastScanIdx = i + j;
            }
            if (endIndex < lastScanIdx || lastScanIdx - i + 1 < searchBytes.length) {
                return -1;
            }
            return i;
        }
        return -1;
    }

    public static class MultiPart {
        private String filename;
        private byte[] bytes;
    }
}
