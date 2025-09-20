import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './style.css';
import config from './config.js';

const columns = [
  'id',
  'name',
  'type',
  'status',
  'pricePerNight',
  'capacity',
  'floor',
  'amenities',
  'description',
];

const HotelManager = () => {
  const [rooms, setRooms] = useState([]);
  const [room, setRoom] = useState({
    id: '',
    name: '',
    type: '',
    status: '',
    pricePerNight: '',
    capacity: '',
    floor: '',
    amenities: '',
    description: '',
  });
  const [idToFetch, setIdToFetch] = useState('');
  const [fetchedRoom, setFetchedRoom] = useState(null);
  const [message, setMessage] = useState('');
  const [editMode, setEditMode] = useState(false);

  // Backend base url (controller is @RequestMapping("/hotelapi"))
  const baseUrl = `${config.url}/hotelapi`;

  useEffect(() => {
    fetchAllRooms();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [baseUrl]);

  const fetchAllRooms = async () => {
    try {
      const res = await axios.get(`${baseUrl}/all`);
      setRooms(Array.isArray(res.data) ? res.data : []);
    } catch (error) {
      const status = error.response?.status ?? '';
      const body = error.response?.data ?? error.message;
      setMessage(`Error ${status}: Failed to fetch rooms. ${typeof body === 'string' ? body : ''}`);
      console.error('GET /all error:', error);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setRoom((prev) => ({ ...prev, [name]: value }));
  };

  const validateForm = () => {
    const required = ['id', 'name', 'type', 'status', 'pricePerNight', 'capacity', 'floor'];
    for (const key of required) {
      if (!room[key] || room[key].toString().trim() === '') {
        setMessage(`Please fill out the ${key} field.`);
        return false;
      }
    }
    if (Number.isNaN(Number(room.id)) || Number(room.id) <= 0) {
      setMessage('id must be a positive number.');
      return false;
    }
    if (Number.isNaN(Number(room.pricePerNight)) || Number(room.pricePerNight) < 0) {
      setMessage('pricePerNight must be a non-negative number.');
      return false;
    }
    if (Number.isNaN(Number(room.capacity)) || Number(room.capacity) <= 0) {
      setMessage('capacity must be a positive number.');
      return false;
    }
    if (Number.isNaN(Number(room.floor))) {
      setMessage('floor must be a number.');
      return false;
    }
    return true;
  };

  const addRoom = async () => {
    if (!validateForm()) return;
    try {
      const payload = {
        ...room,
        id: Number(room.id),
        pricePerNight: Number(room.pricePerNight),
        capacity: Number(room.capacity),
        floor: Number(room.floor),
      };
      await axios.post(`${baseUrl}/add`, payload, {
        headers: { 'Content-Type': 'application/json' },
      });
      setMessage('Room added successfully.');
      await fetchAllRooms();
      resetForm();
    } catch (error) {
      const msg = error.response?.data ?? error.message ?? 'Error adding room.';
      setMessage(typeof msg === 'string' ? msg : 'Error adding room.');
      console.error('POST /add error:', error);
    }
  };

  const updateRoom = async () => {
    if (!validateForm()) return;
    try {
      const payload = {
        ...room,
        id: Number(room.id),
        pricePerNight: Number(room.pricePerNight),
        capacity: Number(room.capacity),
        floor: Number(room.floor),
      };
      await axios.put(`${baseUrl}/update`, payload, {
        headers: { 'Content-Type': 'application/json' },
      });
      setMessage('Room updated successfully.');
      await fetchAllRooms();
      resetForm();
    } catch (error) {
      const msg = error.response?.data ?? error.message ?? 'Error updating room.';
      setMessage(typeof msg === 'string' ? msg : 'Error updating room.');
      console.error('PUT /update error:', error);
    }
  };

  const deleteRoom = async (id) => {
    try {
      const res = await axios.delete(`${baseUrl}/delete/${id}`);
      setMessage(typeof res.data === 'string' ? res.data : 'Room deleted.');
      await fetchAllRooms();
    } catch (error) {
      const msg = error.response?.data ?? error.message ?? 'Error deleting room.';
      setMessage(typeof msg === 'string' ? msg : 'Error deleting room.');
      console.error('DELETE /delete error:', error);
    }
  };

  const getRoomById = async () => {
  try {
    const res = await axios.get(`${baseUrl}/get/${idToFetch}`);
    if (!res.data || Object.keys(res.data).length === 0) {
      setFetchedRoom(null);
      setMessage(`❌ Room with ID ${idToFetch} not found.`);
    } else {
      setFetchedRoom(res.data);
      setMessage('');
    }
  } catch (error) {
    setFetchedRoom(null);
    const msg = error.response?.data || 'Room not found.';
    setMessage(`❌ ${msg}`);
  }
};


  const handleEdit = (r) => {
    setRoom({
      id: String(r.id ?? ''),
      name: String(r.name ?? ''),
      type: String(r.type ?? ''),
      status: String(r.status ?? ''),
      pricePerNight: String(r.pricePerNight ?? ''),
      capacity: String(r.capacity ?? ''),
      floor: String(r.floor ?? ''),
      amenities: String(r.amenities ?? ''),
      description: String(r.description ?? ''),
    });
    setEditMode(true);
    setMessage(`Editing room with ID ${r.id}`);
  };

  const resetForm = () => {
    setRoom({
      id: '',
      name: '',
      type: '',
      status: '',
      pricePerNight: '',
      capacity: '',
      floor: '',
      amenities: '',
      description: '',
    });
    setEditMode(false);
  };

  return (
    <div className="student-container">
      {message && (
        <div className={`message-banner ${String(message).toLowerCase().includes('error') ? 'error' : 'success'}`}>
          {message}
        </div>
      )}

      <h2>Hotel Management — Room Management</h2>

      <div>
        <h3>{editMode ? 'Edit Room Details' : 'Add Room'}</h3>
        <div className="form-grid">
          <input
            type="number"
            name="id"
            placeholder="ID"
            value={room.id}
            onChange={handleChange}
          />
          <input
            type="text"
            name="name"
            placeholder="Room Name (e.g., Deluxe 204)"
            value={room.name}
            onChange={handleChange}
          />

          <select name="type" value={room.type} onChange={handleChange}>
            <option value="">Select Type</option>
            <option value="SINGLE">SINGLE</option>
            <option value="DOUBLE">DOUBLE</option>
            <option value="SUITE">SUITE</option>
          </select>

          <select name="status" value={room.status} onChange={handleChange}>
            <option value="">Select Status</option>
            <option value="AVAILABLE">AVAILABLE</option>
            <option value="OCCUPIED">OCCUPIED</option>
            <option value="MAINTENANCE">MAINTENANCE</option>
          </select>

          <input
            type="number"
            name="pricePerNight"
            placeholder="Price per Night"
            value={room.pricePerNight}
            onChange={handleChange}
          />
          <input
            type="number"
            name="capacity"
            placeholder="Capacity (guests)"
            value={room.capacity}
            onChange={handleChange}
          />
          <input
            type="number"
            name="floor"
            placeholder="Floor"
            value={room.floor}
            onChange={handleChange}
          />
          <input
            type="text"
            name="amenities"
            placeholder="Amenities (comma-separated)"
            value={room.amenities}
            onChange={handleChange}
          />
          <input
            type="text"
            name="description"
            placeholder="Description"
            value={room.description}
            onChange={handleChange}
          />
        </div>

        <div className="btn-group">
          {!editMode ? (
            <button className="btn-blue" onClick={addRoom}>Add Room</button>
          ) : (
            <>
              <button className="btn-green" onClick={updateRoom}>Update Room</button>
              <button className="btn-gray" onClick={resetForm}>Cancel</button>
            </>
          )}
        </div>
      </div>

      <div>
        <h3>Get Room By ID</h3>
        <input
          type="number"
          value={idToFetch}
          onChange={(e) => setIdToFetch(e.target.value)}
          placeholder="Enter ID"
        />
        <button className="btn-blue" onClick={getRoomById}>Fetch</button>

        {fetchedRoom && (
          <div>
            <h4>Room Found:</h4>
            <pre>{JSON.stringify(fetchedRoom, null, 2)}</pre>
          </div>
        )}
      </div>

      <div>
        <h3>All Rooms</h3>
        {rooms.length === 0 ? (
          <p>No rooms found.</p>
        ) : (
          <div className="table-wrapper">
            <table>
              <thead>
                <tr>
                  {columns.map((key) => (
                    <th key={key}>{key}</th>
                  ))}
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {rooms.map((r) => (
                  <tr key={r.id}>
                    {columns.map((key) => (
                      <td key={key}>{String(r?.[key] ?? '')}</td>
                    ))}
                    <td>
                      <div className="action-buttons">
                        <button className="btn-green" onClick={() => handleEdit(r)}>Edit</button>
                        <button className="btn-red" onClick={() => deleteRoom(r.id)}>Delete</button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
};

export default HotelManager;
