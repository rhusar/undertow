package io.undertow.server.protocol.framed;

import java.util.Deque;
import java.util.List;

/**
 *
 * Interface that can be used to determine where to insert a given frame into the pending frame queue.
 *
 * @author Stuart Douglas
 */
public interface FramePriority<C extends AbstractFramedChannel<C, R, S>, R extends AbstractFramedStreamSourceChannel<C, R, S>, S extends AbstractFramedStreamSinkChannel<C, R, S>> {

    /**
     * Inserts the new frame at the correct location in the pending frame list. Note that this must
     * never insert a frame at the very start of the senders list, as this frame has already been activated.
     *
     * This method should return true if the frame was successfully inserted into the pending frame list,
     * if it returns false the frame must not be inserted and will be added to the held frames list instead.
     *
     * Frames held in the held frames list are frames that are not yet ready to be included in the pending frame
     * list, generally because other frames have to be written first.
     *
     * Note that if this method returns true without adding the frame the frame will be dropped.
     *
     * @param newFrame The new frame to insert into the pending frame list
     * @param pendingFrames The pending frame list
     * @return true if the frame can be inserted into the pending frame list
     */
    boolean insertFrame(S newFrame, final List<S> pendingFrames);

    /**
     * Invoked when a new frame is successfully added to the pending frames queue.
     *
     * If frames in the held frame queue are now eligible to be sent they can be added
     * to the pending frames queue.
     *
     * @param addedFrame The newly added frame
     * @param pendingFrames The pending frame queue
     * @param holdFrames The held frame queue
     */
    void frameAdded(S addedFrame, final List<S> pendingFrames, final Deque<S> holdFrames);

}
