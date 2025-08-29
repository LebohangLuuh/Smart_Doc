import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Component, OnInit } from '@angular/core';


interface Appointment {
  id: string;
  time: string;
  type: string;
  patientName: string;
  status: 'confirmed' | 'pending' | 'cancelled';
  color: string;
}

interface DashboardStats {
  appointmentsToday: number;
  pendingAppointments: number;
  totalPatients: number;
}

@Component({
  selector: 'app-doctor-dash',
  imports: [RouterLink, CommonModule],
  templateUrl: './doctor-dash.html',
  styles: ``
})
export class DoctorDash implements OnInit {
  dashboardStats: DashboardStats = {
    appointmentsToday: 10,
    pendingAppointments: 7,
    totalPatients: 136
  };

  menuItems = [
    {
      name: 'Dashboard',
      href: '/dashboard',
      icon: '<path d="M3 4a1 1 0 011-1h12a1 1 0 011 1v2a1 1 0 01-1 1H4a1 1 0 01-1-1V4zM3 10a1 1 0 011-1h6a1 1 0 011 1v6a1 1 0 01-1 1H4a1 1 0 01-1-1v-6zM14 9a1 1 0 00-1 1v6a1 1 0 001 1h2a1 1 0 001-1v-6a1 1 0 00-1-1h-2z"/>',
      active: true
    },
    {
      name: 'Appointments',
      href: '/dashboard',
      icon: '<path fill-rule="evenodd" d="M6 2a1 1 0 00-1 1v1H4a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V6a2 2 0 00-2-2h-1V3a1 1 0 10-2 0v1H7V3a1 1 0 00-1-1zm0 5a1 1 0 000 2h8a1 1 0 100-2H6z" clip-rule="evenodd"/>',
      active: false
    },
    {
      name: 'Patients',
      href: '/dashboard',
      icon: '<path d="M9 12a5 5 0 100-10 5 5 0 000 10zm0 2a7 7 0 100-14 7 7 0 000 14zM13 16a1 1 0 011-1h3a1 1 0 110 2h-3a1 1 0 01-1-1z"/>',
      active: false
    },
    {
      name: 'Report',
      href: '/dashboard',
      icon: '<path d="M2 11a1 1 0 011-1h2a1 1 0 011 1v5a1 1 0 01-1 1H3a1 1 0 01-1-1v-5zM8 7a1 1 0 011-1h2a1 1 0 011 1v9a1 1 0 01-1 1H9a1 1 0 01-1-1V7zM14 4a1 1 0 011-1h2a1 1 0 011 1v12a1 1 0 01-1 1h-2a1 1 0 01-1-1V4z"/>',
      active: false
    },
    {
      name: 'Profile',
      href: '/dashboard',
      icon: '<path fill-rule="evenodd" d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z" clip-rule="evenodd"/>',
      active: false
    },
    {
      name: 'Calendar',
      href: '/dashboard',
      icon: '<path fill-rule="evenodd" d="M6 2a1 1 0 00-1 1v1H4a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V6a2 2 0 00-2-2h-1V3a1 1 0 10-2 0v1H7V3a1 1 0 00-1-1zm0 5a1 1 0 000 2h8a1 1 0 100-2H6z" clip-rule="evenodd"/>',
      active: false
    }
  ];

  appointments: Appointment[] = [
    {
      id: '1',
      time: '10:30 AM',
      type: 'Consultation',
      patientName: 'Andy Man',
      status: 'confirmed',
      color: 'bg-green-500'
    },
    {
      id: '2',
      time: '02:30 PM',
      type: 'Annual Checkup',
      patientName: 'Mpho Tau',
      status: 'confirmed',
      color: 'bg-blue-500'
    },
    {
      id: '3',
      time: '03:30 PM',
      type: 'Vaccination',
      patientName: 'Neo Nathi',
      status: 'confirmed',
      color: 'bg-orange-500'
    },
    {
      id: '4',
      time: '04:30 PM',
      type: 'Follow up Visit',
      patientName: 'Leo Messi',
      status: 'confirmed',
      color: 'bg-purple-500'
    }
  ];

  ngOnInit(): void {
    // Initialize component data
    this.loadDashboardData();
  }
private loadDashboardData(): void {
    // Simulate API call to load dashboard data
    // In real application, this would be a service call
    console.log('Loading dashboard data...');
  }

  getStatusClasses(status: string): string {
    switch (status) {
      case 'confirmed':
        return 'bg-green-100 text-green-800';
      case 'pending':
        return 'bg-yellow-100 text-yellow-800';
      case 'cancelled':
        return 'bg-red-100 text-red-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  }

  rescheduleAppointment(appointmentId: string): void {
    // Handle reschedule logic
    console.log('Rescheduling appointment:', appointmentId);
  }

  searchPatients(query: string): void {
    // Handle patient search
    console.log('Searching patients:', query);
  }


}
